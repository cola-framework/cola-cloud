/*
 * Copyright 2002-${Year} the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cola.libs.jpa.services.impl;

import com.cola.libs.jpa.entities.AbstractEntity;
import com.cola.libs.jpa.services.FlexibleSearchService;
import com.cola.libs.jpa.support.FlexibleQueryBuilder;
import com.cola.libs.jpa.support.JpqlAnalysisConstant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

/**
 * cola
 * Created by jiachen.shi on 7/20/2016.
 */
@Service("flexibleSearchService")
@Transactional(readOnly = true)
public class FlexibleSearchServiceImpl implements FlexibleSearchService {

    @PersistenceContext
    private EntityManager em;

    private enum RelationType {
        ToOne, ToMany
    }

    private class RelationShip {
        private FetchType fetchType;
        private RelationType relationType;

        public FetchType getFetchType() {
            return fetchType;
        }

        public void setFetchType(FetchType fetchType) {
            this.fetchType = fetchType;
        }

        public RelationType getRelationType() {
            return relationType;
        }

        public void setRelationType(RelationType relationType) {
            this.relationType = relationType;
        }
    }

    private <T extends AbstractEntity> RelationShip getRelationShip(Class<T> tClass, AccessibleObject accessibleObject) {
        Assert.notNull(accessibleObject, "The Field must not be null!");
        RelationShip relationShip = new RelationShip();
        try {
            ManyToOne manyToOne = accessibleObject.getAnnotation(ManyToOne.class);
            if (manyToOne != null) {
                relationShip.setFetchType(manyToOne.fetch());
                relationShip.setRelationType(RelationType.ToOne);
            } else {
                OneToOne oneToOne = accessibleObject.getAnnotation(OneToOne.class);
                if (oneToOne != null) {
                    relationShip.setFetchType(oneToOne.fetch());
                    relationShip.setRelationType(RelationType.ToOne);
                } else {
                    OneToMany oneToMany = accessibleObject.getAnnotation(OneToMany.class);
                    if (oneToMany != null) {
                        relationShip.setFetchType(oneToMany.fetch());
                        relationShip.setRelationType(RelationType.ToMany);
                    } else {
                        ManyToMany manyToMany = accessibleObject.getAnnotation(ManyToMany.class);
                        if (manyToMany != null) {
                            relationShip.setFetchType(manyToMany.fetch());
                            relationShip.setRelationType(RelationType.ToMany);
                        } else {
                            if (accessibleObject instanceof Field) {
                                Field field = (Field) accessibleObject;
                                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), tClass);
                                Method getMethod = pd.getReadMethod();
                                if (getMethod != null) {
                                    relationShip = this.getRelationShip(tClass, getMethod);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return relationShip;
    }

    private Query covertQueryFromFlexibleQueryBuilder(FlexibleQueryBuilder builder, Class<?> resultClass) {
        Query query;
        if (resultClass != null) {
            query = this.em.createQuery(builder.toJPQL(), resultClass);
        } else {
            query = this.em.createQuery(builder.toJPQL());
        }
        if (builder.getParamList().size() > 0) {
            int i = 1;
            for (Object p : builder.getParamList()) {
                query.setParameter(i, p);
                i++;
            }
        }
        if (builder.getParamMap().keySet() != null && builder.getParamMap().keySet().size() > 0) {
            for (String name : builder.getParamMap().keySet()) {
                query.setParameter(name, builder.getParamMap().get(name));
            }
        }
        return query;
    }

    private String covertCountQuery(String jpql) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        StringBuilder result = new StringBuilder("select count(*) ");
        String uc = jpql.toUpperCase().trim();
        result.append(jpql.substring(uc.indexOf(JpqlAnalysisConstant.Clause.FROM.name())));
        return result.toString();
    }

    private Query coverCountQuery(FlexibleQueryBuilder builder){
        Assert.notNull(builder, "The FlexibleQueryBuilder must not be null!");
        String jpql = builder.toJPQL();
        StringBuilder result = new StringBuilder("select count(*) ");
        String uc = jpql.toUpperCase().trim();
        result.append(jpql.substring(uc.indexOf(JpqlAnalysisConstant.Clause.FROM.name())));
        FlexibleQueryBuilder countBuilder = new FlexibleQueryBuilder(result.toString());
        countBuilder.addParameters(builder.getParamList());
        countBuilder.setParameters(builder.getParamMap());
        return covertQueryFromFlexibleQueryBuilder(countBuilder, Long.class);
    }

    private <T extends AbstractEntity> void addFetchJoin(Class<T> tClass, Root root) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(root, "The Root must not be null!");
        EntityType<T> model = root.getModel();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            RelationShip r = this.getRelationShip(tClass, field);
            if (FetchType.EAGER.equals(r.getFetchType())) {
                if (RelationType.ToOne.equals(r.getRelationType())) {
                    root.fetch(model.getSingularAttribute(field.getName(), field.getType()), JoinType.LEFT);
                } else if (RelationType.ToMany.equals(r.getRelationType())) {
                    if (Collection.class.isAssignableFrom(field.getType())) {
                        Type genericType = field.getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            Class genericClazz = (Class) pt.getActualTypeArguments()[0];
                            if (List.class.isAssignableFrom(field.getType())) {
                                root.fetch(model.getList(field.getName(), genericClazz), JoinType.LEFT);
                            } else if (Set.class.isAssignableFrom(field.getType())) {
                                root.fetch(model.getSet(field.getName(), genericClazz), JoinType.LEFT);
                            } else {
                                root.fetch(model.getCollection(field.getName(), genericClazz), JoinType.LEFT);
                            }
                        }
                    } else {
                        root.fetch(model.getMap(field.getName()), JoinType.LEFT);
                    }
                }
            }
        }
    }

    private <P> Specification covertSpecificationFromMap(Map<String, P> condition) {
        return new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (condition != null && condition.keySet() != null && condition.keySet().size() > 0) {
                    Iterator<String> var = condition.keySet().iterator();
                    while (var.hasNext()) {
                        String key = var.next();
                        list.add(criteriaBuilder.equal(root.get(key).as(condition.get(key).getClass()), condition.get(key)));
                    }
                }
                if (list.size() > 0) {
                    Predicate[] p = new Predicate[list.size()];
                    return criteriaBuilder.and(list.toArray(p));
                }
                return null;
            }
        };
    }

    protected <T extends AbstractEntity> TypedQuery<Long> getCountQuery(Class<T> tClass) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return this.getCountQuery(tClass, (Map) null);
    }

    protected <T extends AbstractEntity, P> TypedQuery<Long> getCountQuery(Class<T> tClass, Map<String, P> condition) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return this.getCountQuery(tClass, covertSpecificationFromMap(condition));
    }

    protected <T extends AbstractEntity> TypedQuery<Long> getCountQuery(Class<T> tClass, Specification<T> spec) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Long.class);
        Root root = query.from(tClass);
        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, builder);
            if (predicate != null) {
                query.where(predicate);
            }
        }
        return this.em.createQuery(query);
    }

    protected <T extends AbstractEntity> TypedQuery<T> getQuery(Class<T> tClass, Specification<T> spec, Sort sort) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(tClass);
        Root root = query.from(tClass);
        this.addFetchJoin(tClass, root);
        query.select(root);
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, builder);
            if (predicate != null) {
                query.where(predicate);
            }
        }
        if (sort != null) {
            query.orderBy(QueryUtils.toOrders(sort, root, builder));
        }
        return this.em.createQuery(query);
    }

    protected <T extends AbstractEntity> Page readPage(Query query, Pageable pageable, Class<T> tClass) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Long total = this.count(tClass);
        List content = total.longValue() > (long) pageable.getOffset() ? query.getResultList() : Collections.emptyList();
        return new PageImpl(content, pageable, total.longValue());
    }

    protected <T extends AbstractEntity> Page readPage(Query query, Pageable pageable, String jpql) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Long total = this.em.createQuery(this.covertCountQuery(jpql), Long.class).getSingleResult();
        List content = total.longValue() > (long) pageable.getOffset() ? query.getResultList() : Collections.emptyList();
        return new PageImpl(content, pageable, total.longValue());

    }

    protected <T extends AbstractEntity, P> Page readPage(Query query, Pageable pageable, FlexibleQueryBuilder builder) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Long total = (Long) this.coverCountQuery(builder).getSingleResult();
        List content = total.longValue() > (long) pageable.getOffset() ? query.getResultList() : Collections.emptyList();
        return new PageImpl(content, pageable, total.longValue());
    }

    @Override
    public <T extends AbstractEntity> long count(Class<T> tClass) {
        return ((Long) this.getCountQuery(tClass).getSingleResult()).longValue();
    }

    @Override
    public <T extends AbstractEntity, V> long count(Class<T> tClass, Map<String, V> condition) {
        return ((Long) this.getCountQuery(tClass, condition).getSingleResult()).longValue();

    }

    @Override
    public <T extends AbstractEntity> long count(Class<T> tClass, Specification<T> spec) {
        return ((Long) this.getCountQuery(tClass, spec).getSingleResult()).longValue();
    }

    @Override
    public <T extends AbstractEntity, V> T uniqueQuery(Class<T> tClass, Map<String, V> condition) {
        return (T) this.getQuery(tClass, this.covertSpecificationFromMap(condition), null).getSingleResult();
    }

    @Override
    public <T extends AbstractEntity> T uniqueQuery(Class<T> tClass, Specification<T> spec) {
        return this.getQuery(tClass, spec, null).getSingleResult();
    }

    @Override
    public <T> T uniqueQuery(FlexibleQueryBuilder builder, Class<T> resultClass) {
        Assert.notNull(builder, "The FlexibleQueryBuilder must not be null!");
        return (T) this.covertQueryFromFlexibleQueryBuilder(builder, resultClass).getSingleResult();
    }

    @Override
    public <T> T uniqueQuery(String jpql, Class<T> resultClass) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = this.em.createQuery(jpql);
        return (T) query.getSingleResult();
    }

    @Override
    public <T extends AbstractEntity> Iterable<T> query(Class<T> tClass, Specification<T> spec, Sort sort) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return this.getQuery(tClass, spec, sort).getResultList();
    }

    @Override
    public <T extends AbstractEntity, P> Iterable<T> query(Class<T> tClass, Map<String, P> condition, Sort sort) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return this.getQuery(tClass, this.covertSpecificationFromMap(condition), sort).getResultList();
    }

    @Override
    public <T> Iterable<T> query(String jpql, Class<T> resultClass) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query;
        if (resultClass != null) {
            query = this.em.createQuery(jpql, resultClass);
        } else {
            query = this.em.createQuery(jpql);
        }
        return query.getResultList();
    }

    @Override
    public <T> Iterable<T> query(FlexibleQueryBuilder builder, Class<T> resultClass) {
        Assert.notNull(builder, "The FlexibleQueryBuilder must not be null!");
        return this.covertQueryFromFlexibleQueryBuilder(builder, resultClass).getResultList();
    }

    @Override
    public <T extends AbstractEntity> Page<T> pagingQuery(Class<T> tClass, Specification<T> spec, Pageable page) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Sort sort = page == null ? null : page.getSort();
        TypedQuery query = this.getQuery(tClass, spec, sort);
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, tClass));
    }

    @Override
    public <T extends AbstractEntity, P> Page<T> pagingQuery(Class<T> tClass, Map<String, P> condition, Pageable page) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Sort sort = page == null ? null : page.getSort();
        TypedQuery query = this.getQuery(tClass, this.covertSpecificationFromMap(condition), sort);
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, tClass));
    }

    @Override
    public <T> Page<T> pagingQuery(String jpql, Class<T> resultClass, Pageable page) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query;
        if (resultClass != null) {
            query = this.em.createQuery(jpql, resultClass);
        } else {
            query = this.em.createQuery(jpql);
        }
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, jpql));
    }

    @Override
    public <T> Page<T> pagingQuery(FlexibleQueryBuilder builder, Class<T> resultClass, Pageable page) {
        Assert.notNull(builder, "The FlexibleQueryBuilder must not be null!");
        Query query = this.covertQueryFromFlexibleQueryBuilder(builder, resultClass);
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, builder));
    }
}

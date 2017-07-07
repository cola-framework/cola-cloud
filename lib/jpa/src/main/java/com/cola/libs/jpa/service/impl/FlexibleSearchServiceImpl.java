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
package com.cola.libs.jpa.service.impl;

import com.cola.libs.jpa.entity.AbstractEntity;
import com.cola.libs.jpa.service.FlexibleSearchService;
import com.cola.libs.jpa.support.FlexibleQueryBuilder;
import com.cola.libs.jpa.support.JpqlAnalysisConstant;
import com.cola.libs.jpa.support.QueryHintConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * cola
 * Created by jiachen.shi on 7/20/2016.
 */
@Transactional(readOnly = true)
public class FlexibleSearchServiceImpl implements FlexibleSearchService {

    private static Logger logger = LoggerFactory.getLogger(FlexibleSearchServiceImpl.class);

    @Value("${spring.jpa.properties.hibernate.cache.use_query_cache:false}")
    private boolean useQueryCache;

    @PersistenceContext
    private EntityManager em;

    private <T> Query createQuery(String jpql, Class<T> tClass){
        if(tClass == null){
            return this.em.createQuery(jpql);
        }/*else{
            if(!Object[].class.equals(tClass) && !Tuple.class.equals(tClass)){
                Query query = this.em.createQuery(jpql);
                if(Map.class.isAssignableFrom(tClass)){
                    HibernateQuery hibernateQuery = query.unwrap(HibernateQuery.class);
                    if(hibernateQuery != null){
                        hibernateQuery.getHibernateQuery().setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                    }
                }else if(List.class.isAssignableFrom(tClass)){
                    HibernateQuery hibernateQuery = query.unwrap(HibernateQuery.class);
                    if(hibernateQuery != null){
                        hibernateQuery.getHibernateQuery().setResultTransformer(Transformers.TO_LIST);
                    }
                }else{
                    Session session = (Session)this.em.getDelegate();
                    if(session != null){
                        org.hibernate.query.Query hqlQuery = session.createQuery(jpql);
                        if(hqlQuery != null && hqlQuery.getReturnTypes().length > 1){
                            HibernateQuery hibernateQuery = query.unwrap(HibernateQuery.class);
                            if(hibernateQuery != null){
                                hibernateQuery.getHibernateQuery().setResultTransformer(Transformers.aliasToBean(tClass));
                            }
                        }
                    }
                }
                query.setHint(QueryHintConstant.CACHEABLE, this.useQueryCache);
                return query;
            }
        }*/
        TypedQuery<T> query = this.em.createQuery(jpql, tClass);
        query.setHint(QueryHintConstant.CACHEABLE, this.useQueryCache);
        return query;
    }

    private <T> Query createNativeQuery(String sql, Class<T> tClass){
        if(tClass == null){
            return this.em.createNativeQuery(sql);
        }/*else{
            if(!Object[].class.equals(tClass)){
                Query query = this.em.createNativeQuery(sql);
                NativeQuery sqlQuery = query.unwrap(NativeQuery.class);
                if(Map.class.isAssignableFrom(tClass)){
                    if(sqlQuery != null){
                        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                    }
                }else if(List.class.isAssignableFrom(tClass)){
                    if(sqlQuery != null){
                        sqlQuery.setResultTransformer(Transformers.TO_LIST);
                    }
                }else{
                    if(sqlQuery != null){
                        sqlQuery.setResultTransformer(Transformers.aliasToBean(tClass));
                    }
                }
                query.setHint(QueryHintConstant.CACHEABLE, this.useQueryCache);
                return query;
            }
        }*/
        Query nativeQuery = this.em.createNativeQuery(sql, tClass);
        nativeQuery.setHint(QueryHintConstant.CACHEABLE, this.useQueryCache);
        return nativeQuery;
    }

    private Query covertQueryFromFlexibleQueryBuilder(FlexibleQueryBuilder builder, Class<?> resultClass) {
        Query query = this.createQuery(builder.toJPQL(), resultClass);
        if (builder.getParamList().size() > 0) {
            int i = 0;
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

    private CriteriaQuery coverCountQuery(CriteriaQuery query){
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        Set<Root<?>> roots = query.getRoots();
        if(roots != null){
            Root<?> next = roots.iterator().next();
            if (query.isDistinct()) {
                query.select(builder.countDistinct(next));
            } else {
                query.select(builder.count(next));
            }
        }
        return query;
    }

    private Path getPath(Path p, String key){
        return p.get(key);
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
                        String[] split = key.split(JpqlAnalysisConstant.SPLIT_ALIAS_REFRENCE);
                        Path p = root.get(split[0]);
                        if(split.length > 1){
                            for(int i=1;i<split.length;i++){
                                p = getPath(p, split[i]);
                            }
                        }
                        list.add(criteriaBuilder.equal(p.as(condition.get(key).getClass()), condition.get(key)));
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
        TypedQuery typedQuery = this.em.createQuery(query);
        typedQuery.setHint(QueryHintConstant.CACHEABLE, this.useQueryCache);
        return typedQuery;
    }

    protected <T extends AbstractEntity> TypedQuery<T> getQuery(Class<T> tClass, Specification<T> spec, Sort sort, Map<String, Object> properties) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(tClass);
        Root root = query.from(tClass);
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
        TypedQuery<T> t = this.em.createQuery(query);
        t.setHint(QueryHintConstant.CACHEABLE, this.useQueryCache);
        if(properties != null && properties.keySet() != null && properties.keySet().size() > 0){
            for(String key:properties.keySet()){
                t.setHint(key, properties.get(key));
            }
        }
        return t;
    }

    protected <T extends AbstractEntity> Page readNativePage(Query query, Pageable pageable, String sql) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Long total = (Long)this.em.createNativeQuery(this.covertCountQuery(sql), Long.class).getSingleResult();
        List content = total.longValue() > (long) pageable.getOffset() ? query.getResultList() : Collections.emptyList();
        return new PageImpl(content, pageable, total.longValue());
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

    protected <T extends AbstractEntity> Page readPage(Query query, Pageable pageable, CriteriaQuery builder) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Long total = (Long)this.em.createQuery(this.coverCountQuery(builder)).getSingleResult();
        List content = total.longValue() > (long) pageable.getOffset() ? query.getResultList() : Collections.emptyList();
        return new PageImpl(content, pageable, total.longValue());
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder(){
        return this.em.getCriteriaBuilder();
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
    public <T extends AbstractEntity, V> T uniqueQuery(Class<T> tClass, Map<String, V> condition, Map<String ,Object> properties) {
        List<T> resultList = this.getQuery(tClass, this.covertSpecificationFromMap(condition), null, properties).getResultList();
        if(resultList != null && resultList.size() > 0){
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public <T extends AbstractEntity> T uniqueQuery(Class<T> tClass, Specification<T> spec, Map<String ,Object> properties) {
        List<T> resultList = this.getQuery(tClass, spec, null, properties).getResultList();
        if(resultList != null && resultList.size() > 0){
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public <T> T uniqueQuery(FlexibleQueryBuilder builder, Class<T> resultClass) {
        Assert.notNull(builder, "The FlexibleQueryBuilder must not be null!");
        List<T> resultList = this.covertQueryFromFlexibleQueryBuilder(builder, resultClass).getResultList();
        if(resultList != null && resultList.size() > 0){
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public <T> T uniqueQuery(String jpql, Class<T> resultClass) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = this.createQuery(jpql, resultClass);
        List<T> resultList = query.getResultList();
        if(resultList != null && resultList.size() > 0){
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public <T extends AbstractEntity> List<T> query(Class<T> tClass, Specification<T> spec, Sort sort, Map<String ,Object> properties) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return this.getQuery(tClass, spec, sort, properties).getResultList();
    }

    @Override
    public <T extends AbstractEntity, P> List<T> query(Class<T> tClass, Map<String, P> condition, Sort sort, Map<String ,Object> properties) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return this.getQuery(tClass, this.covertSpecificationFromMap(condition), sort, properties).getResultList();
    }

    @Override
    public <T> List<T> query(String jpql, Class<T> resultClass) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = this.createQuery(jpql, resultClass);
        return query.getResultList();
    }

    @Override
    public <T> List<T> query(FlexibleQueryBuilder builder, Class<T> resultClass) {
        Assert.notNull(builder, "The FlexibleQueryBuilder must not be null!");
        return this.covertQueryFromFlexibleQueryBuilder(builder, resultClass).getResultList();
    }

    @Override
    public <T> List<T> query(CriteriaQuery<T> query){
        Assert.notNull(query, "The Criteria Query must not be null!");
        TypedQuery<T> typedQuery = this.em.createQuery(query);
        typedQuery.setHint(QueryHintConstant.CACHEABLE, this.useQueryCache);
        return typedQuery.getResultList();
    }

    @Override
    public <T> List<T> nativeQuery(String sql, Class<T> resultClass) {
        Assert.notNull(sql, "The SQL must not be null!");
        Query query = this.createNativeQuery(sql, resultClass);
        return query.getResultList();
    }

    @Override
    public <T extends AbstractEntity> Page<T> pagingQuery(Class<T> tClass, Specification<T> spec, Pageable page, Map<String ,Object> properties) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Sort sort = page == null ? null : page.getSort();
        TypedQuery query = this.getQuery(tClass, spec, sort, properties);
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, tClass));
    }

    @Override
    public <T extends AbstractEntity, P> Page<T> pagingQuery(Class<T> tClass, Map<String, P> condition, Pageable page, Map<String ,Object> properties) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Sort sort = page == null ? null : page.getSort();
        TypedQuery query = this.getQuery(tClass, this.covertSpecificationFromMap(condition), sort, properties);
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, tClass));
    }

    @Override
    public <T> Page<T> pagingQuery(String jpql, Class<T> resultClass, Pageable page) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = this.createQuery(jpql, resultClass);
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, jpql));
    }

    @Override
    public <T> Page<T> pagingQuery(FlexibleQueryBuilder builder, Class<T> resultClass, Pageable page) {
        Assert.notNull(builder, "The FlexibleQueryBuilder must not be null!");
        Query query = this.covertQueryFromFlexibleQueryBuilder(builder, resultClass);
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, builder));
    }

    @Override
    public <T> Page<T> pagingNativeQuery(String sql, Class<T> resultClass, Pageable page){
        Assert.notNull(sql, "The SQL must not be null!");
        Query nativeQuery = this.createNativeQuery(sql, resultClass);
        return (Page<T>) (page == null ? new PageImpl<T>(nativeQuery.getResultList()) : this.readNativePage(nativeQuery, page, sql));
    }

    @Override
    public <T> Page<T> pagingQuery(CriteriaQuery<T> builder, Pageable page) {
        Assert.notNull(builder, "The Criteria Query must not be null!");
        TypedQuery<T> query = this.em.createQuery(builder);
        query.setHint(QueryHintConstant.CACHEABLE, this.useQueryCache);
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, builder));
    }

}

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
import com.cola.libs.jpa.support.JpqlAnalysisConstant;
import com.mysql.fabric.xmlrpc.base.Params;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * cola
 * Created by jiachen.shi on 7/20/2016.
 */
@Service("flexibleSearchService")
@Transactional(readOnly = true)
public class FlexibleSearchServiceImpl implements FlexibleSearchService {

    @PersistenceContext
    private EntityManager em;

    protected <T extends AbstractEntity> String getCountQueryString(Class<T> tClass) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        String countQuery = String.format("select count(x) from %s x");
        return QueryUtils.getQueryString(countQuery, tClass.getSimpleName());
    }

    protected <T extends AbstractEntity, V extends Serializable> String getCountQueryString(Class<T> tClass, Map<String, V> condition) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(condition, "The Condition must not be null!");
        Assert.notNull(condition.keySet(), "The Condition must not be null!");
        String placeholder = "x";
        return QueryUtils.getExistsQueryString(tClass.getSimpleName(), placeholder, condition.keySet());
    }

    protected <T extends AbstractEntity, V extends Serializable> String getQueryString(Class<T> tClass, Map<String, V> condition) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(condition, "The Condition must not be null!");
        Assert.notNull(condition.keySet(), "The Condition must not be null!");
        StringBuilder sb = new StringBuilder(String.format("from %s x", new Object[]{tClass.getSimpleName()}));
        if (condition != null && condition.keySet() != null && condition.keySet().size() > 0) {
            Iterator<String> var = condition.keySet().iterator();
            sb.append(" WHERE ");
            while (var.hasNext()) {
                String idAttribute = (String) var.next();
                sb.append(String.format("%s.%s = :%s", new Object[]{"x", idAttribute, idAttribute}));
                sb.append(" AND ");
            }
            sb = new StringBuilder(sb.substring(0, sb.lastIndexOf("AND")));
        }
        return sb.toString();
    }

    protected String covertCountQuery(String jpql){
        Assert.notNull(jpql, "The JPQL must not be null!");
        StringBuilder result = new StringBuilder("select count(*) ");
        String uc = jpql.toUpperCase().trim();
        result.append(jpql.substring(uc.indexOf(JpqlAnalysisConstant.Clause.FROM.name()) + 4));
        return result.toString();
    }

    protected <T extends AbstractEntity> TypedQuery<T> getQuery(Class<T> tClass, Sort sort) {
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(tClass);
        Root root = query.from(tClass);
        query.select(root);
        if (sort != null) {
            query.orderBy(QueryUtils.toOrders(sort, root, builder));
        }

        return this.em.createQuery(query);
    }

    protected <T extends AbstractEntity> TypedQuery<T> getQuery(Class<T> tClass, Pageable pageable) {
        Sort sort = pageable == null?null:pageable.getSort();
        return this.getQuery(tClass, sort);
    }

    protected <T extends AbstractEntity> Page readPage(Query query, Pageable pageable, Class<T> tClass) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Long total = count(tClass);
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

    protected <T extends AbstractEntity, P extends Serializable> Page readPage(Query query, Pageable pageable, String jpql, Iterable<P> parames) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        TypedQuery<Long> countQuery = this.em.createQuery(this.covertCountQuery(jpql), Long.class);
        if (parames != null && parames.iterator().hasNext()) {
            int i = 1;
            for (P p : parames) {
                countQuery.setParameter(i, p);
                i++;
            }
        }
        Long total = countQuery.getSingleResult();
        List content = total.longValue() > (long) pageable.getOffset() ? query.getResultList() : Collections.emptyList();
        return new PageImpl(content, pageable, total.longValue());
    }

    protected <T extends AbstractEntity, P extends Serializable> Page readPage(Query query, Pageable pageable, String jpql, Map<String, P> parames) {
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        TypedQuery<Long> countQuery = this.em.createQuery(this.covertCountQuery(jpql), Long.class);
        if (parames != null && parames.keySet() != null) {
            for (String name : parames.keySet()) {
                countQuery.setParameter(name, parames.get(name));
            }
        }
        Long total = countQuery.getSingleResult();
        List content = total.longValue() > (long) pageable.getOffset() ? query.getResultList() : Collections.emptyList();
        return new PageImpl(content, pageable, total.longValue());
    }

    @Override
    public <T extends AbstractEntity> long count(Class<T> entityClass) {
        return ((Long)this.em.createQuery(this.getCountQueryString(entityClass), Long.class).getSingleResult()).longValue();
    }

    @Override
    public <T extends AbstractEntity, V extends Serializable> long count(Class<T> tClass, Map<String, V> condition) {
        TypedQuery query = this.em.createQuery(this.getCountQueryString(tClass, condition), Long.class);
        for (String key : condition.keySet()) {
            query.setParameter(key, condition.get(key));
        }
        return ((Long) query.getSingleResult()).longValue();
    }

    @Override
    public <T extends AbstractEntity, V extends Serializable> T uniqueQuery(Class<T> tClass, Map<String, V> condition) {
        TypedQuery<T> query = this.em.createQuery(this.getQueryString(tClass, condition), tClass);
        if (condition != null && condition.keySet() != null && condition.keySet().size() > 0) {
            for (String key : condition.keySet()) {
                query.setParameter(key, condition.get(key));
            }
        }
        return query.getSingleResult();
    }

    @Override
    public <T extends Number> T aggregatedQuery(String jpql) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = this.em.createQuery(jpql);
        return (T) query.getSingleResult();
    }

    @Override
    public <T extends Number, P extends Serializable> T aggregatedQuery(String jpql, Iterable<P> parames) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = this.em.createQuery(jpql);
        if (parames != null && parames.iterator().hasNext()) {
            int i = 1;
            for (P p : parames) {
                query.setParameter(i, p);
                i++;
            }
        }
        return (T) query.getSingleResult();
    }

    @Override
    public <T extends Number, P extends Serializable> T aggregatedQuery(String jpql, Map<String, P> parames) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = em.createQuery(jpql);
        if (parames != null && parames.keySet() != null) {
            for (String name : parames.keySet()) {
                query.setParameter(name, parames.get(name));
            }
        }
        return (T) query.getSingleResult();
    }

    @Override
    public <T extends AbstractEntity> Iterable<T> findAll(Class<T> tClass) {
        TypedQuery<T> query = this.em.createQuery(this.getQueryString(tClass, null), tClass);
        return query.getResultList();
    }

    @Override
    public <T extends AbstractEntity> Iterable<T> findAll(Class<T> tClass, Sort sort) {
        return this.getQuery(tClass, (Sort) sort).getResultList();
    }

    @Override
    public <T extends AbstractEntity> Page<T> findAll(Class<T> tClass, Pageable page) {
        TypedQuery query = this.getQuery(tClass, page);
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, tClass));
    }

    @Override
    public <T> Iterable<T> query(String jpql) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = this.em.createQuery(jpql);
        return query.getResultList();
    }

    @Override
    public <T, P extends Serializable> Iterable<T> query(String jpql, Iterable<P> parames) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = this.em.createQuery(jpql);
        if (parames != null && parames.iterator().hasNext()) {
            int i = 1;
            for (P p : parames) {
                query.setParameter(i, p);
                i++;
            }
        }
        return query.getResultList();
    }

    @Override
    public <T, P extends Serializable> Iterable<T> query(String jpql, Map<String, P> parames) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = em.createQuery(jpql);
        if (parames != null && parames.keySet() != null) {
            for (String name : parames.keySet()) {
                query.setParameter(name, parames.get(name));
            }
        }
        return query.getResultList();
    }

    @Override
    public <T> Page<T> pagingQuery(String jpql, Pageable page) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = em.createQuery(jpql);
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, jpql));
    }

    @Override
    public <T, P extends Serializable> Iterable<T> pagingQuery(String jpql, Iterable<P> parames, Pageable page) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = em.createQuery(jpql);
        if (parames != null && parames.iterator().hasNext()) {
            int i = 1;
            for (P p : parames) {
                query.setParameter(i, p);
                i++;
            }
        }
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query,page, jpql, parames));
    }

    @Override
    public <T, P extends Serializable> Iterable<T> pagingQuery(String jpql, Map<String, P> parames, Pageable page) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        Query query = em.createQuery(jpql);
        if (parames != null && parames.keySet() != null) {
            for (String name : parames.keySet()) {
                query.setParameter(name, parames.get(name));
            }
        }
        return (Page<T>) (page == null ? new PageImpl<T>(query.getResultList()) : this.readPage(query, page, jpql, parames));
    }

}

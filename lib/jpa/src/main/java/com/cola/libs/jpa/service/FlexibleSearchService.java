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
package com.cola.libs.jpa.service;

import com.cola.libs.jpa.entity.AbstractEntity;
import com.cola.libs.jpa.support.FlexibleQueryBuilder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * cola
 * Created by jiachen.shi on 7/20/2016.
 */
public interface FlexibleSearchService {

    /**
     * Gets criteria builder.
     * @return the criteria builder
     */
    public CriteriaBuilder getCriteriaBuilder();

    /**
     * Count long.
     * @param <T>         the type parameter
     * @param entityClass the entity class
     * @return the long
     */
    public <T extends AbstractEntity> long count(Class<T> entityClass);

    /**
     * Count long.
     * @param <T>         the type parameter
     * @param <V>         the type parameter
     * @param entityClass the entity class
     * @param condition   the condition
     * @return the long
     */
    public <T extends AbstractEntity, V> long count(Class<T> entityClass, Map<String, V> condition);

    /**
     * Count long.
     * @param <T>    the type parameter
     * @param tClass the t class
     * @param spec   the spec
     * @return the long
     */
    public <T extends AbstractEntity> long count(Class<T> tClass, Specification<T> spec);

    /**
     * Unique query t.
     * @param <T>        the type parameter
     * @param <V>        the type parameter
     * @param tClass     the t class
     * @param condition  the condition
     * @param properties the properties
     * @return the t
     */
    public <T extends AbstractEntity, V> T uniqueQuery(Class<T> tClass, Map<String, V> condition, Map<String ,Object> properties);

    /**
     * Unique query t.
     * @param <T>        the type parameter
     * @param tClass     the t class
     * @param spec       the spec
     * @param properties the properties
     * @return the t
     */
    public <T extends AbstractEntity> T uniqueQuery(Class<T> tClass, Specification<T> spec, Map<String ,Object> properties);

    /**
     * Aggregated query t.
     * @param <T>         the type parameter
     * @param jpql        the jpql
     * @param returnClass the return class
     * @return the t
     */
    public <T> T uniqueQuery(String jpql, Class<T> returnClass);

    /**
     * Unique query t.
     * @param <T>         the type parameter
     * @param builder     the builder
     * @param returnClass the return class
     * @return the t
     */
    public <T> T uniqueQuery(FlexibleQueryBuilder builder, Class<T> returnClass);

    /**
     * Query iterable.
     * @param <T>        the type parameter
     * @param tClass     the t class
     * @param spec       the spec
     * @param sort       the sort
     * @param properties the properties
     * @return the iterable
     */
    public <T extends AbstractEntity> List<T> query(Class<T> tClass, Specification<T> spec, Sort sort, Map<String ,Object> properties);

    /**
     * Query iterable.
     * @param <T>        the type parameter
     * @param <P>        the type parameter
     * @param tClass     the t class
     * @param condition  the condition
     * @param sort       the sort
     * @param properties the properties
     * @return the iterable
     */
    public <T extends AbstractEntity, P> List<T> query(Class<T> tClass, Map<String, P> condition, Sort sort, Map<String ,Object> properties);

    /**
     * Query iterable.
     * @param <T>         the type parameter
     * @param builder     the builder
     * @param resultClass the result class
     * @return the iterable
     */
    public <T> List<T> query(FlexibleQueryBuilder builder, Class<T> resultClass);

    /**
     * Query iterable.
     * @param <T>         the type parameter
     * @param jpql        the jpql
     * @param resultClass the result class
     * @return the iterable
     */
    public <T> List<T> query(String jpql, Class<T> resultClass);

    /**
     * Query iterable.
     * @param <T>   the type parameter
     * @param query the query
     * @return the iterable
     */
    public <T> List<T> query(CriteriaQuery<T> query);

    /**
     * Native query iterable.
     * @param <T>         the type parameter
     * @param sql         the sql
     * @param resultClass the result class
     * @return the iterable
     */
    public <T> List<T> nativeQuery(String sql, Class<T> resultClass);

    /**
     * Paging query iterable.
     * @param <T>        the type parameter
     * @param tClass     the t class
     * @param spec       the spec
     * @param page       the page
     * @param properties the properties
     * @return the iterable
     */
    public <T extends AbstractEntity> Page<T> pagingQuery(Class<T> tClass, Specification<T> spec, Pageable page, Map<String ,Object> properties);

    /**
     * Paging query iterable.
     * @param <T>        the type parameter
     * @param <P>        the type parameter
     * @param tClass     the t class
     * @param condition  the condition
     * @param page       the page
     * @param properties the properties
     * @return the iterable
     */
    public <T extends AbstractEntity, P> Page<T> pagingQuery(Class<T> tClass, Map<String, P> condition, Pageable page, Map<String ,Object> properties);

    /**
     * Query page.
     * @param <T>         the type parameter
     * @param jpql        the jpql
     * @param resultClass the result class
     * @param page        the page
     * @return the page
     */
    public <T> Page<T> pagingQuery(String jpql, Class<T> resultClass, Pageable page);

    /**
     * Paging query iterable.
     * @param <T>         the type parameter
     * @param builder     the builder
     * @param resultClass the result class
     * @param page        the page
     * @return the iterable
     */
    public <T> Page<T> pagingQuery(FlexibleQueryBuilder builder, Class<T> resultClass, Pageable page);

    /**
     * Paging native query page.
     * @param <T>         the type parameter
     * @param sql         the sql
     * @param resultClass the result class
     * @param page        the page
     * @return the page
     */
    public <T> Page<T> pagingNativeQuery(String sql, Class<T> resultClass, Pageable page);

    /**
     * Paging query page.
     * @param <T>     the type parameter
     * @param builder the builder
     * @param page    the page
     * @return the page
     */
    public <T> Page<T> pagingQuery(CriteriaQuery<T> builder, Pageable page);

}

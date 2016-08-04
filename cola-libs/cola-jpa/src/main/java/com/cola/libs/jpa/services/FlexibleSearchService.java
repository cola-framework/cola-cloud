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
package com.cola.libs.jpa.services;

import com.cola.libs.jpa.entities.AbstractEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

/**
 * cola
 * Created by jiachen.shi on 7/20/2016.
 */
public interface FlexibleSearchService {

    /**
     * Find all iterable.
     * @param <T>    the type parameter
     * @param tClass the t class
     * @return the iterable
     */
    public <T extends AbstractEntity> Iterable<T> findAll(Class<T> tClass);

    /**
     * Find all iterable.
     * @param <T>    the type parameter
     * @param tClass the t class
     * @param sort   the sort
     * @return the iterable
     */
    public <T extends AbstractEntity> Iterable<T> findAll(Class<T> tClass, Sort sort);

    /**
     * Find all page.
     * @param <T>    the type parameter
     * @param tClass the t class
     * @param page   the page
     * @return the page
     */
    public <T extends AbstractEntity> Page<T> findAll(Class<T> tClass, Pageable page);

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
     * Unique query t.
     * @param <T>       the type parameter
     * @param <V>       the type parameter
     * @param tClass    the t class
     * @param condition the condition
     * @return the t
     */
    public <T extends AbstractEntity, V> T uniqueQuery(Class<T> tClass, Map<String, V> condition);

    /**
     * Aggregated query t.
     * @param <T>  the type parameter
     * @param jpql the jpql
     * @return the t
     */
    public <T extends Number> T aggregatedQuery(String jpql);

    /**
     * Aggregated query t.
     * @param <T>     the type parameter
     * @param <P>     the type parameter
     * @param jpql    the jpql
     * @param parames the parames
     * @return the t
     */
    public <T extends Number, P> T aggregatedQuery(String jpql, Iterable<P> parames);

    /**
     * Aggregated query t.
     * @param <T>     the type parameter
     * @param <P>     the type parameter
     * @param jpql    the jpql
     * @param parames the parames
     * @return the t
     */
    public <T extends Number, P> T aggregatedQuery(String jpql, Map<String, P> parames);

    /**
     * Query iterable.
     * @param <T>  the type parameter
     * @param jpql the jpql
     * @return the iterable
     */
    public <T> Iterable<T> query(String jpql);

    /**
     * Query iterable.
     * @param <T>     the type parameter
     * @param <P>     the type parameter
     * @param jpql    the jpql
     * @param parames the parames
     * @return the iterable
     */
    public <T, P> Iterable<T> query(String jpql, Iterable<P> parames);

    /**
     * Query iterable.
     * @param <T>     the type parameter
     * @param <P>     the type parameter
     * @param jpql    the jpql
     * @param parames the parames
     * @return the iterable
     */
    public <T, P> Iterable<T> query(String jpql, Map<String, P> parames);

    /**
     * Query page.
     * @param <T>  the type parameter
     * @param jpql the jpql
     * @param page the page
     * @return the page
     */
    public <T> Page<T> pagingQuery(String jpql, Pageable page);

    /**
     * Paging query iterable.
     * @param <T>     the type parameter
     * @param <P>     the type parameter
     * @param jpql    the jpql
     * @param parames the parames
     * @param page    the page
     * @return the iterable
     */
    public <T, P> Iterable<T> pagingQuery(String jpql, Iterable<P> parames, Pageable page);

    /**
     * Paging query iterable.
     * @param <T>     the type parameter
     * @param <P>     the type parameter
     * @param jpql    the jpql
     * @param parames the parames
     * @param page    the page
     * @return the iterable
     */
    public <T, P> Iterable<T> pagingQuery(String jpql, Map<String, P> parames, Pageable page);

}

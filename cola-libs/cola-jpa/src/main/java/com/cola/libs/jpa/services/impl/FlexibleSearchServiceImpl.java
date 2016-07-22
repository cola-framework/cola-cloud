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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * cola
 * Created by jiachen.shi on 7/20/2016.
 */
@Service("flexibleSearchService")
@Transactional(readOnly = true)
public class FlexibleSearchServiceImpl implements FlexibleSearchService {

    @PersistenceContext
    private EntityManager em;

    private <T extends AbstractEntity> String getCountQueryString(Class<T> tClass) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        String countQuery = String.format("select count(x) from %s x");
        return QueryUtils.getQueryString(countQuery, tClass.getSimpleName());
    }

    @Override
    public <T extends AbstractEntity> long count(Class<T> entityClass) {
        return ((Long)this.em.createQuery(this.getCountQueryString(entityClass), Long.class).getSingleResult()).longValue();
    }

    @Override
    public <T extends AbstractEntity, V extends Serializable> T uniqueQuery(Class<T> tClass, Map<String, V> condition) {
        return null;
    }

    @Override
    public <T extends AbstractEntity> Iterable<T> findAll(Class<T> tClass) {
        return null;
    }

    @Override
    public <T extends AbstractEntity> Iterable<T> findAll(Class<T> tClass, Sort sort) {
        return null;
    }

    @Override
    public <T extends AbstractEntity> Page<T> findAll(Class<T> tClass, Pageable page) {
        return null;
    }

    @Override
    public <T> Iterable<T> query(String jpql) {
        return null;
    }

    @Override
    public <T, P extends Serializable> Iterable<T> query(String jpql, Iterable<P> parames) {
        return null;
    }

    @Override
    public <T, P extends Serializable> Iterable<T> query(String jpql, Map<String, P> parames) {
        return null;
    }

    @Override
    public <T> Page<T> pagingQuery(String jpql, Pageable page) {
        return null;
    }

    @Override
    public <T, P extends Serializable> Iterable<T> pagingQuery(String jpql, Iterable<P> parames, Pageable page) {
        return null;
    }

    @Override
    public <T, P extends Serializable> Iterable<T> pagingQuery(String jpql, Map<String, P> parames, Pageable page) {
        return null;
    }

}

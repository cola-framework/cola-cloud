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

/**
 * cola
 * Created by jiachen.shi on 7/20/2016.
 */
public interface FlexibleSearchService {

    public <T extends AbstractEntity> Iterable<T> findAll(Class<T> tClass);

    public <T extends AbstractEntity> Iterable<T> findAll(Class<T> tClass, Sort sort);

    public <T extends AbstractEntity> Page<T> findAll(Class<T> tClass, Pageable page);

    public <T extends AbstractEntity> long count(Class<T> entityClass);

    public <T> Iterable<T> query(String jpql);

    public <T> Page<T> query(String jpql, Pageable page);

}

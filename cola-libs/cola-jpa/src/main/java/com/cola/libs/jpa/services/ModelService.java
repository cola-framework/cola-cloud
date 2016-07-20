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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

/**
 * cola
 * Created by jiachen.shi on 7/18/2016.
 */
public interface ModelService {

    public <T extends AbstractEntity> T save(T entity);

    public <T extends AbstractEntity> List<T> save(Iterable<T> entities);

    public <T extends AbstractEntity, ID extends Serializable> void delete(Class<T> tClass, ID id);

    public <T extends AbstractEntity> void delete(T entity);

    public <T extends AbstractEntity> void delete(Iterable<? extends T> entities);

    public <T extends AbstractEntity> void deleteAll(Class<T> entityClass);

    public <T extends AbstractEntity> void deleteInBatch(Iterable<T> entities);

    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id);

    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id, LockModeType lockModeType);

    public <T extends AbstractEntity, ID extends Serializable> T get(Class<T> tClass, ID id);

    public int execute(String jpql);

    public <P extends Serializable> int execute(String jpql, Iterable<P> parames);

    public <P extends Serializable> int execute(String jpql, Map<String, P> parames);

    public <T extends AbstractEntity, ID extends Serializable> boolean exists(Class<T> entityClass, ID id);

}

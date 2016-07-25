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
import com.cola.libs.jpa.services.ModelService;
import com.cola.libs.jpa.support.QueryTranslatorHelper;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * cola
 * Created by jiachen.shi on 7/18/2016.
 */
@Service("modelService")
@Transactional(readOnly = true)
public class ModelServiceImpl implements ModelService {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public <T extends AbstractEntity> T save(T entity){
        Assert.notNull(entity, "The entity must not be null!");
        JpaEntityInformation<T, ?> entityInformation = JpaEntityInformationSupport.getMetadata((Class<T>) entity.getClass(), em);
        Date now = new Date();
        entity.setLastModifiedTime(now);
        if(entityInformation.isNew(entity)) {
            entity.setCreateTime(now);
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Override
    @Transactional
    public <T extends AbstractEntity> List<T> save(Iterable<T> entities) {
        ArrayList result = new ArrayList();
        if(entities == null) {
            return result;
        } else {
            Iterator var3 = entities.iterator();

            while(var3.hasNext()) {
                T entity = (T) var3.next();
                result.add(this.save(entity));
            }
            em.flush();
            return result;
        }
    }

    @Override
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id){
        return load(tClass, id, null);
    }

    @Override
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id, LockModeType type){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        return type==null?this.em.find(tClass, id):this.em.find(tClass,id,type);
    }

    @Override
    public <T extends AbstractEntity, ID extends Serializable> T get(Class<T> tClass, ID id){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        return this.em.getReference(tClass, id);
    }

    @Override
    @Transactional
    public int  execute(String jpql){
        return execute(jpql, new HashMap<String, Object>());
    }

    @Override
    @Transactional
    public int execute(String jpql, Iterable<Object> parames) {
        Assert.notNull(jpql, "The JPQL must not be null!");
        jpql = QueryTranslatorHelper.appendVersionIncrementForUpdate(jpql);
        Query query = em.createQuery(jpql);
        if(parames != null && parames.iterator().hasNext()){
            int i = 1;
            for(Object p:parames){
                query.setParameter(i, p);
                i++;
            }
        }
        return query.executeUpdate();
    }

    @Override
    @Transactional
    public int execute(String jpql, Map<String, Object> parames){
        Assert.notNull(jpql, "The JPQL must not be null!");
        jpql = QueryTranslatorHelper.appendVersionIncrementForUpdate(jpql);
        Query query = em.createQuery(jpql);
        if(parames != null && parames.keySet() != null ){
            for(String name:parames.keySet()){
                query.setParameter(name, parames.get(name));
            }
        }
        return query.executeUpdate();
    }

    @Override
    @Transactional
    public <T extends AbstractEntity, ID extends Serializable> void delete(Class<T> tClass, ID id) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        T entity = this.load(tClass, id);
        if(entity == null) {
            throw new EmptyResultDataAccessException(String.format("No %s entity with id %s exists!", new Object[]{tClass, id}), 1);
        } else {
            this.delete(entity);
        }
    }

    @Override
    @Transactional
    public <T extends AbstractEntity> void delete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        this.em.remove(this.em.contains(entity)?entity:this.em.merge(entity));
    }

    private <T extends AbstractEntity> String getDeleteAllQueryString(Class<T> tClass) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return QueryUtils.getQueryString("delete from %s x", tClass.getSimpleName());
    }

    @Override
    @Transactional
    public <T extends AbstractEntity> void deleteAll(Class<T> tClass) {
        this.em.createQuery(this.getDeleteAllQueryString(tClass)).executeUpdate();
    }

    @Override
    @Transactional
    public <T extends AbstractEntity> void delete(Iterable<? extends T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        Iterator var2 = entities.iterator();

        while(var2.hasNext()) {
            T entity = (T) var2.next();
            this.delete(entity);
        }
    }

    @Override
    @Transactional
    public <T extends AbstractEntity> void deleteInBatch(Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        if(entities.iterator().hasNext()) {
            T next = entities.iterator().next();
            QueryUtils.applyAndBind(QueryUtils.getQueryString("delete from %s x", next.getClass().getSimpleName()), entities, this.em).executeUpdate();
        }
    }

    @Override
    public <T extends AbstractEntity, ID extends Serializable> boolean exists(Class<T> tClass, ID id) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        JpaEntityInformation<T, ?> entityInformation = JpaEntityInformationSupport.getMetadata(tClass, em);
        if(entityInformation.getIdAttribute() == null) {
            return this.load(tClass, id) != null;
        } else {
            String placeholder = "x";
            String entityName = tClass.getSimpleName();
            Iterable idAttributeNames = entityInformation.getIdAttributeNames();
            String existsQuery = QueryUtils.getExistsQueryString(entityName, placeholder, idAttributeNames);
            TypedQuery query = this.em.createQuery(existsQuery, Long.class);
            if(!entityInformation.hasCompositeId()) {
                query.setParameter((String)idAttributeNames.iterator().next(), id);
                return ((Long)query.getSingleResult()).longValue() == 1L;
            } else {
                Iterator var7 = idAttributeNames.iterator();

                while(var7.hasNext()) {
                    String idAttributeName = (String)var7.next();
                    Object idAttributeValue = entityInformation.getCompositeIdAttributeValue(id, idAttributeName);
                    boolean complexIdParameterValueDiscovered = idAttributeValue != null && !query.getParameter(idAttributeName).getParameterType().isAssignableFrom(idAttributeValue.getClass());
                    if(complexIdParameterValueDiscovered) {
                        return this.load(tClass, id) != null;
                    }

                    query.setParameter(idAttributeName, idAttributeValue);
                }

                return ((Long)query.getSingleResult()).longValue() == 1L;
            }
        }
    }

}

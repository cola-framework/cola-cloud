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
import com.cola.libs.jpa.service.ModelService;
import com.cola.libs.jpa.support.QueryHintConstant;
import com.cola.libs.jpa.support.QueryTranslatorHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;

/**
 * cola
 * Created by jiachen.shi on 7/18/2016.
 */
@Transactional(readOnly = true)
public class ModelServiceImpl implements ModelService {

    private static Logger logger = LoggerFactory.getLogger(ModelServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    private <T extends AbstractEntity> String getDeleteAllQueryString(Class<T> tClass) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return QueryUtils.getQueryString("delete from %s x", tClass.getSimpleName());
    }

    @Override
    public EntityGraph getEntityGraph(String str){
        Assert.notNull(str, "The entityGraph name must not be null!");
        return this.em.getEntityGraph(str);
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> tClass){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return this.em.createEntityGraph(tClass);
    }

    @Override
    public EntityTransaction getCurrentTransacntion(){
        return this.em.getTransaction();
    }

    @Override
    public void clear(){
        this.em.clear();
    }

    @Override
    @Transactional
    @CacheEvict(value="dbCache", key = "#entity.getClass().getName()+ ':' + #entity.getId()", beforeInvocation = true)
    public <T extends AbstractEntity> T save(T entity){
        Assert.notNull(entity, "The entity must not be null!");
        JpaEntityInformation<T, ?> entityInformation = JpaEntityInformationSupport.getMetadata((Class<T>) entity.getClass(), em);
        Date now = new Date();
        entity.setLastModifiedTime(now);
        if(entityInformation.isNew(entity)) {
            entity.setCreateTime(now);
            entity.setDeleted(Boolean.FALSE);
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }
        em.clear();
        return entity;
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
    @Cacheable(value="dbCache", key = "#tClass.getName()+ ':' + #id", unless = "#result == null")
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id){
        return load(tClass, id, null, null);
    }

    @Override
    @Cacheable(value="dbCache", key = "#tClass.getName()+ ':' + #id", unless = "#result == null")
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id, LockModeType type){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        return this.em.find(tClass, id, type);
    }

    @Override
    @Cacheable(value="dbCache", key = "#tClass.getName()+ ':' + #id", unless = "#result == null")
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id, String entityGraphName){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        Map<String, Object> properties = null;
        if(!StringUtils.isEmpty(entityGraphName)){
            properties = new HashMap<>();
            properties.put(QueryHintConstant.FETCH_GRAPH, this.em.getEntityGraph(entityGraphName));
        }
        return this.em.find(tClass, id, properties);
    }

    @Override
    @Cacheable(value="dbCache", key = "#tClass.getName()+ ':' + #id", unless = "#result == null")
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id, Map<String, Object> properties){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        return this.em.find(tClass, id, properties);
    }

    @Override
    @Cacheable(value="dbCache", key = "#tClass.getName()+ ':' + #id", unless = "#result == null")
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id, LockModeType type, Map<String, Object> properties){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        return this.em.find(tClass, id, type, properties);
    }

    @Override
    @Cacheable(value="dbCache", key = "#tClass.getName()+ ':' + #id", unless = "#result == null")
    public <T extends AbstractEntity, ID extends Serializable> T get(Class<T> tClass, ID id){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        return this.em.getReference(tClass, id);
    }

    @Override
    @Transactional
    @CacheEvict(value="dbCache", keyGenerator = "jpqlKeyGenerator", beforeInvocation = true)
    public int  execute(String jpql){
        return execute(jpql, new HashMap<String, Object>());
    }

    @Override
    @Transactional
    @CacheEvict(value="dbCache", keyGenerator = "jpqlKeyGenerator", beforeInvocation = true)
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
    @CacheEvict(value="dbCache", keyGenerator = "jpqlKeyGenerator", beforeInvocation = true)
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
    @CacheEvict(value="dbCache", key = "#criteria.getRoot().getJavaType().getName()+ ':*'", beforeInvocation = true)
    public int execute(CriteriaUpdate criteria){
        Assert.notNull(criteria, "The CriteriaUpdate must not be null!");
        Query query = this.em.createQuery(criteria);
        return query.executeUpdate();
    }

    @Override
    @Transactional
    @CacheEvict(value="dbCache", key = "#criteria.getRoot().getJavaType().getName()+ ':*'", beforeInvocation = true)
    public int execute(CriteriaDelete criteria){
        Assert.notNull(criteria, "The CriteriaDelete must not be null!");
        Query query = this.em.createQuery(criteria);
        return query.executeUpdate();
    }

    @Override
    @Transactional
    public <T, S> T executeStoredProcedure(String storedProcedureName, List<S> params, String outParamName, Class<T> resultClass){
        Assert.notNull(storedProcedureName, "The Stored Procedure Name must not be null!");
        StoredProcedureQuery namedStoredProcedureQuery = this.em.createNamedStoredProcedureQuery(storedProcedureName);
        if(params != null && params.size() > 0){
            int i = 0;
            for(S s:params){
                namedStoredProcedureQuery.setParameter(i, s);
                i++;
            }
        }
        if(!StringUtils.isEmpty(outParamName)){
            return (T)namedStoredProcedureQuery.getOutputParameterValue(outParamName);
        }else{
            return (T)Boolean.valueOf(namedStoredProcedureQuery.execute());
        }
    }

    @Override
    @Transactional
    public <T, S> T executeStoredProcedure(String storedProcedureName, Map<String, S> params, String outParamName, Class<T> resultClass){
        Assert.notNull(storedProcedureName, "The Stored Procedure Name must not be null!");
        StoredProcedureQuery namedStoredProcedureQuery = this.em.createNamedStoredProcedureQuery(storedProcedureName);
        if(params != null && params.keySet() != null && params.keySet().size() > 0){
            for(String s:params.keySet()){
                namedStoredProcedureQuery.setParameter(s, params.get(s));
            }
        }
        if(!StringUtils.isEmpty(outParamName)){
            return (T)namedStoredProcedureQuery.getOutputParameterValue(outParamName);
        }else{
            return (T)Boolean.valueOf(namedStoredProcedureQuery.execute());
        }
    }

    @Override
    @Transactional
    @CacheEvict(value="dbCache", key = "#tClass.getName()+ ':' + #id", beforeInvocation = true)
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
    @CacheEvict(value="dbCache", key = "#entity.getClass().getName()+ ':' + #entity.getId()", beforeInvocation = true)
    public <T extends AbstractEntity> void delete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        this.em.remove(this.em.contains(entity)?entity:this.em.merge(entity));
    }

    @Override
    @Transactional
    @CacheEvict(value="dbCache", key = "#tClass.getName() + ':*'", beforeInvocation = true)
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
    @CacheEvict(keyGenerator = "entitiesKeyGenerator", beforeInvocation = true)
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

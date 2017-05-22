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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
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

    protected  <T extends AbstractEntity> String getPhysicalDelQueryString(Class<T> tClass) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return QueryUtils.getQueryString("delete from %s x", tClass.getSimpleName());
    }

    protected  <T extends AbstractEntity> String getLogicalDelQueryString(Class<T> tClass) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return QueryUtils.getQueryString("update %s x set x.deleted = true", tClass.getSimpleName());
    }

    @Override
    public EntityGraph getEntityGraph(String entityGraphName){
        Assert.notNull(entityGraphName, "The EntityGraphName must not be null!");
        return this.em.getEntityGraph(entityGraphName);
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> tClass){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        return this.em.createEntityGraph(tClass);
    }

    @Override
    public EntityTransaction getTransacntion(){
        return this.em.getTransaction();
    }

    @Override
    @Transactional
    public <T extends AbstractEntity> T save(T entity){
        Assert.notNull(entity, "The entity must not be null!");
        JpaEntityInformation<T, ?> entityInformation = JpaEntityInformationSupport.getEntityInformation((Class<T>) entity.getClass(), em);
        if(entityInformation.isNew(entity)) {
            this.setDefaultValue(entity, new ArrayList<>());
            em.persist(entity);
            //em.refresh(entity);
        } else {
            entity.setLastModifiedTime(new Date());
            entity = em.merge(entity);
            em.flush();
        }
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
            return result;
        }
    }

    @Override
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id){
        return load(tClass, id, null, null);
    }

    @Override
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id, LockModeType type){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        return load(tClass, id, type, null);
    }

    @Override
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id, EntityGraph<T> entityGraph){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        Map<String, Object> properties = new HashMap<>();
        if(entityGraph != null){
            properties.put(QueryHintConstant.FETCH_GRAPH, entityGraph);
        }
        return load(tClass, id, null, properties);
    }

    @Override
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id, Map<String, Object> properties){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        return load(tClass, id, null, properties);
    }

    @Override
    public <T extends AbstractEntity, ID extends Serializable> T load(Class<T> tClass, ID id, LockModeType type, Map<String, Object> properties){
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        Map<String, Object> hints = new HashMap<>();
        hints.put(QueryHintConstant.CACHE_RETRIEVE_MODE, CacheRetrieveMode.USE);
        hints.put(QueryHintConstant.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
        if(properties != null){
            for(String key:properties.keySet()){
                hints.put(key, properties.get(key));
            }
        }
        return this.em.find(tClass, id, type, hints);
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
    public int execute(CriteriaUpdate criteria){
        Assert.notNull(criteria, "The CriteriaUpdate must not be null!");
        Query query = this.em.createQuery(criteria);
        return query.executeUpdate();
    }

    @Override
    @Transactional
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
    public <T extends AbstractEntity, ID extends Serializable> void delete(Class<T> tClass, ID id, Boolean deleted) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        T entity = this.load(tClass, id);
        if(entity == null) {
            throw new EmptyResultDataAccessException(String.format("No %s entity with id %s containsKey!", new Object[]{tClass, id}), 1);
        } else {
            this.delete(entity, deleted);
        }
    }

    @Override
    @Transactional
    public <T extends AbstractEntity> void delete(T entity, Boolean deleted) {
        Assert.notNull(entity, "The entity must not be null!");
        entity = this.em.contains(entity) ? entity : this.load((Class<T>) entity.getClass(), entity.getId());
        if (deleted == null || !deleted) {
            entity.setDeleted(true);
            em.merge(entity);
        } else {
            this.em.remove(entity);
        }
    }

    @Override
    @Transactional
    public <T extends AbstractEntity> void delete(Iterable<? extends T> entities, Boolean deleted) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        Iterator var2 = entities.iterator();

        while(var2.hasNext()) {
            T entity = (T) var2.next();
            this.delete(entity, deleted);
        }
    }

    @Override
    @Transactional
    public <T extends AbstractEntity> void deleteAll(Class<T> tClass, Boolean deleted) {
        if(deleted == null || !deleted){
            this.em.createQuery(this.getLogicalDelQueryString(tClass)).executeUpdate();
        }else{
            this.em.createQuery(this.getPhysicalDelQueryString(tClass)).executeUpdate();
        }
    }

    @Override
    @Transactional
    public <T extends AbstractEntity> void deleteInBatch(Iterable<T> entities, Boolean deleted) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        if(entities.iterator().hasNext()) {
            T next = entities.iterator().next();
            if(deleted == null || !deleted){
                QueryUtils.applyAndBind(this.getLogicalDelQueryString(next.getClass()), entities, this.em).executeUpdate();
            }else{
                QueryUtils.applyAndBind(this.getPhysicalDelQueryString(next.getClass()), entities, this.em).executeUpdate();
            }
        }
    }

    @Override
    public <T extends AbstractEntity, ID extends Serializable> boolean exists(Class<T> tClass, ID id) {
        Assert.notNull(tClass, "The EntityClass must not be null!");
        Assert.notNull(id, "The given id must not be null!");
        JpaEntityInformation<T, ?> entityInformation = JpaEntityInformationSupport.getEntityInformation(tClass, em);
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

    protected <T extends AbstractEntity> void setDefaultValue(T entity, List<Object> history){

        if(history == null){
            history = new ArrayList<>();
        }
        history.add(entity);

        entity.setCreateTime(new Date());
        entity.setLastModifiedTime(new Date());
        entity.setDeleted(Boolean.FALSE);

        Class clazz = entity.getClass();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            PropertyDescriptor pd = null;
            try {
                pd = new PropertyDescriptor(field.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object o = getMethod.invoke(entity);
                if(o != null && !history.contains(o)){
                    if(AbstractEntity.class.isAssignableFrom(o.getClass())){
                        this.setDefaultValue((AbstractEntity) o, history);
                    }else if(Iterable.class.isAssignableFrom(o.getClass())){
                        for(Object item: (Iterable)o){
                            if(AbstractEntity.class.isAssignableFrom(item.getClass())){
                                this.setDefaultValue((AbstractEntity) item, history);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("Failed fill default value for entity.", e);
            }
        }
    }
}

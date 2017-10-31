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
package com.cola.lib.cache.hibernate.strategy;

import com.cola.lib.cache.hibernate.IntensiveCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.access.SoftLock;
import org.jetbrains.annotations.Nullable;
import org.springframework.cache.Cache;

/**
 * cola
 * Created by jiachen.shi on 8/29/2016.
 */
public abstract class AccessStrategyAdapter {

    protected Log logger = LogFactory.getLog(AccessStrategyAdapter.class);
    protected Cache cache;
    protected final CacheDataDescription metaData;
    protected SessionFactoryOptions settings;

    protected AccessStrategyAdapter(Cache cache, SessionFactoryOptions settings, CacheDataDescription metaData) {
        this.cache = cache;
        this.metaData = metaData;
        this.settings = settings;
    }

    protected Class<?> getReturnClassFromKey(Object key) {
        Class<?> resultClass = null;
/*        if(key instanceof CacheKey){
            CacheKey cacheKey = (CacheKey)key;
            String entityOrRoleName = cacheKey.getEntityOrRoleName();
            if (!StringUtils.isEmpty(entityOrRoleName)) {
                try {
                    resultClass = Class.forName(entityOrRoleName);
                } catch (ClassNotFoundException e) {
                    logger.error("Convert Class From CacheKey has error.", e);
                }
            }
        }else{
            resultClass = Object.class;
        }*/
        return resultClass;
    }

    protected Object getActualKey(Object key){
/*        if(key instanceof CacheKey){
            CacheKey cacheKey = (CacheKey) key;
            String entityOrRoleName = cacheKey.getEntityOrRoleName();
            return entityOrRoleName + ":" + cacheKey.getKey();
        }*/
        return key;
    }

    @Nullable
    protected Object get(Object key) throws CacheException {
        Object actualKey = getActualKey(key);
        logger.debug("AccessStrategyAdapter get method start. key:" + actualKey);
        return this.cache.get(actualKey, getReturnClassFromKey(key));
    }

    protected boolean putFromLoad(Object key, Object val) throws CacheException {
        return putFromLoad(key, val, settings.isMinimalPutsEnabled());
    }

    protected boolean putFromLoad(Object key, Object val, boolean minimalPutOverride) throws CacheException {
        IntensiveCache intensiveCache = (IntensiveCache)this.cache;
        Object actualKey = getActualKey(key);
        logger.debug("AccessStrategyAdapter putFromLoad method start. key:" + actualKey + " val:" + val);
        if (minimalPutOverride && intensiveCache.containsKey(actualKey)) {
            return false;
        } else {
            this.cache.put(actualKey, val);
            return true;
        }
    }

    @Nullable
    protected SoftLock lockRegion() throws CacheException {
        logger.debug("AccessStrategyAdapter lockRegion method start.");
        return null;
    }

    protected void unlockRegion(SoftLock lock) throws CacheException {
        logger.debug("AccessStrategyAdapter unlockRegion method start.");
    }

    protected final void removeAll() throws CacheException {
        logger.debug("AccessStrategyAdapter removeAll method start.");
        this.evictAll();
    }

    protected void evictAll() throws CacheException {
        logger.debug("AccessStrategyAdapter evictAll method start.");
        this.cache.clear();
    }

    protected void evict(Object key) throws CacheException {
        Object actualKey = getActualKey(key);
        logger.debug("AccessStrategyAdapter evict method start. key:" + actualKey);
        this.cache.evict(actualKey);
    }

    @Nullable
    public abstract SoftLock lock(Object key) throws CacheException;

    public abstract void unlock(Object key, SoftLock lock) throws CacheException;

    public abstract boolean update(Object key, Object val) throws CacheException;

    public abstract boolean afterUpdate(Object key, Object val, SoftLock lock) throws CacheException;

    public abstract boolean insert(Object key, Object val) throws CacheException;

    public abstract boolean afterInsert(Object key, Object val) throws CacheException;

    public abstract void remove(Object key) throws CacheException;

}

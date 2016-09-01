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
package com.cola.libs.cache.hibernate.strategy;

import com.cola.libs.cache.hibernate.IntensiveCache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CacheKey;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;
import org.jetbrains.annotations.Nullable;
import org.springframework.cache.Cache;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * cola
 * Created by jiachen.shi on 8/29/2016.
 */
public abstract class RedisAccessStrategyAdapter {

    protected Log logger = LogFactory.getLog(RedisAccessStrategyAdapter.class);
    protected Cache cache;
    protected final CacheDataDescription metaData;
    protected final Settings settings;

    protected RedisAccessStrategyAdapter(Cache cache, Settings settings, CacheDataDescription metaData) {
        this.cache = cache;
        this.metaData = metaData;
        this.settings = settings;
    }

    protected String convertKeyFromCacheKey(CacheKey cacheKey) {
        String entityOrRoleName = cacheKey.getEntityOrRoleName();
        Serializable key = cacheKey.getKey();
        return entityOrRoleName + ":" + key;
    }

    protected Class<?> convertClassFromCacheKey(CacheKey cacheKey) {
        String entityOrRoleName = cacheKey.getEntityOrRoleName();
        Class<?> resultClass = null;
        if (!StringUtils.isEmpty(entityOrRoleName)) {
            try {
                resultClass = Class.forName(entityOrRoleName);
            } catch (ClassNotFoundException e) {
                logger.error("Convert Class From CacheKey has error.", e);
            }
        }
        return resultClass;
    }

    @Nullable
    protected Object get(Object key) throws CacheException {
        CacheKey cacheKey = (CacheKey) key;
        logger.debug("RedisAccessStrategyAdapter get method start. key:" + cacheKey);
        return this.cache.get(convertKeyFromCacheKey(cacheKey), convertClassFromCacheKey(cacheKey));
    }

    protected boolean putFromLoad(Object key, Object val) throws CacheException {
        return putFromLoad(key, val, settings.isMinimalPutsEnabled());
    }

    protected boolean putFromLoad(Object key, Object val, boolean minimalPutOverride) throws CacheException {
        IntensiveCache intensiveCache = (IntensiveCache)this.cache;
        String convertKeyFromCacheKey = convertKeyFromCacheKey((CacheKey) key);
        logger.debug("RedisAccessStrategyAdapter putFromLoad method start. key:" + convertKeyFromCacheKey + " val:" + val);
        if (minimalPutOverride && intensiveCache.exists(convertKeyFromCacheKey)) {
            return false;
        } else {
            this.cache.put(convertKeyFromCacheKey, val);
            return true;
        }
    }

    @Nullable
    protected SoftLock lockRegion() throws CacheException {
        logger.debug("RedisAccessStrategyAdapter lockRegion method start.");
        return null;
    }

    protected void unlockRegion(SoftLock lock) throws CacheException {
        logger.debug("RedisAccessStrategyAdapter unlockRegion method start.");
    }

    protected final void removeAll() throws CacheException {
        logger.debug("RedisAccessStrategyAdapter removeAll method start.");
        this.evictAll();
    }

    protected void evictAll() throws CacheException {
        logger.debug("RedisAccessStrategyAdapter evictAll method start.");
        this.cache.clear();
    }

    protected void evict(Object key) throws CacheException {
        String cacheKey = convertKeyFromCacheKey((CacheKey) key);
        logger.debug("RedisAccessStrategyAdapter evict method start. key:" + cacheKey);
        this.cache.evict(cacheKey);
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

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
public class ReadWriteAccessStrategy extends AccessStrategyAdapter {

    protected Log logger = LogFactory.getLog(ReadWriteAccessStrategy.class);

    public ReadWriteAccessStrategy(Cache cache, SessionFactoryOptions settings, CacheDataDescription metaData) {
        super(cache, settings, metaData);
    }

    @Nullable
    @Override
    public SoftLock lock(Object key) throws CacheException {
        Object cacheKey = getActualKey(key);
        logger.debug("ReadWriteAccessStrategy lock method start. key:" + cacheKey);
        return null;
    }

    @Override
    public void unlock(Object key, SoftLock lock) throws CacheException {
        Object cacheKey = getActualKey(key);
        logger.debug("ReadWriteAccessStrategy unlock method start. key:" + cacheKey);
    }

    @Override
    public boolean update(Object key, Object value) throws CacheException {
        Object cacheKey = getActualKey(key);
        logger.debug("ReadWriteAccessStrategy update method start. key:" + cacheKey + " val:" + value);
        this.cache.evict(cacheKey);
        return false;
    }

    @Override
    public boolean afterUpdate(Object key, Object value, SoftLock lock) throws CacheException {
        Object cacheKey = getActualKey(key);
        logger.debug("ReadWriteAccessStrategy afterUpdate method start. key:" + cacheKey + " val:" + value);
        return false;
    }

    @Override
    public boolean insert(Object key, Object value) throws CacheException {
        Object cacheKey = getActualKey(key);
        logger.debug("ReadWriteAccessStrategy insert method start. key:"+cacheKey + " val:"+value);
        return false;
    }

    @Override
    public boolean afterInsert(Object key, Object value) throws CacheException {
        Object cacheKey = getActualKey(key);
        logger.debug("ReadWriteAccessStrategy afterInsert method start. key:"+cacheKey + " val:"+value);
        return false;
    }

    @Override
    public void remove(Object key) throws CacheException {
        Object cacheKey = getActualKey(key);
        logger.debug("ReadWriteAccessStrategy remove method start. key:"+cacheKey);
        this.cache.evict(cacheKey);
    }
}

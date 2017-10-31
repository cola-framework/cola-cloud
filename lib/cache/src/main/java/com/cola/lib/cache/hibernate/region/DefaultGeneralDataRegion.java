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
package com.cola.lib.cache.hibernate.region;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.GeneralDataRegion;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.springframework.cache.Cache;

import java.util.Properties;

/**
 * cola
 * Created by jiachen.shi on 8/29/2016.
 */
public class DefaultGeneralDataRegion extends AbstractRegion implements GeneralDataRegion {

    protected Log logger = LogFactory.getLog(DefaultGeneralDataRegion.class);

    public DefaultGeneralDataRegion(String regionName, Cache cache, Properties properties) {
        super(regionName, cache, properties);
    }

    @Override
    public Object get(SharedSessionContractImplementor sharedSessionContractImplementor, Object key) throws CacheException {
        Object actualKey = getActualKey(key);
        logger.debug("DefaultGeneralDataRegion get method start. key:" + actualKey);
        return this.cache.get(actualKey, this.getReturnClassFromKey(key));
    }

    @Override
    public void put(SharedSessionContractImplementor sharedSessionContractImplementor, Object key, Object value) throws CacheException {
        Object actualKey = getActualKey(key);
        logger.debug("DefaultGeneralDataRegion put method start. key:" + actualKey + " value:" + value);
        this.cache.put(actualKey, value);
    }

    @Override
    public void evict(Object key) throws CacheException {
        Object actualKey = getActualKey(key);
        logger.debug("DefaultGeneralDataRegion evict method start. key:" + actualKey);
        this.cache.evict(actualKey);
    }

    @Override
    public void evictAll() throws CacheException {
        logger.debug("DefaultGeneralDataRegion evictAll method start.");
        this.cache.clear();
    }
}

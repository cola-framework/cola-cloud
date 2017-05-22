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
package com.cola.libs.cache.hibernate;

import com.cola.libs.cache.hibernate.region.CollectionRegion;
import com.cola.libs.cache.hibernate.region.*;
import com.cola.libs.cache.management.CacheManagerFactory;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.*;
import org.hibernate.cache.spi.access.AccessType;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

import java.util.Properties;

/**
 * cola
 * Created by jiachen.shi on 8/26/2016.
 */
public class CustomizeRegionFactory implements RegionFactory {

    private SessionFactoryOptions settings;
    private CacheManager cacheManager;

    @Override
    public void start(SessionFactoryOptions settings, Properties properties) throws CacheException {
        this.cacheManager = CacheManagerFactory.getCacheManager();
        this.settings = settings;
        Assert.notNull(this.cacheManager, "cacheManager is required, CacheManagerFactory must be init first");
    }

    @Override
    public void stop() {
        this.cacheManager = null;
    }

    @Override
    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }

    @Override
    public AccessType getDefaultAccessType() {
        return AccessType.READ_WRITE;
    }

    @Override
    public long nextTimestamp() {
        return System.currentTimeMillis();
    }

    @Override
    public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metaData) throws CacheException {
        return new DefaultEntityRegion(regionName, this.cacheManager.getCache(regionName), properties, this.settings, metaData);
    }

    @Override
    public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metaData) throws CacheException {
        return new CustomizeNaturalIdRegion(regionName, this.cacheManager.getCache(regionName), properties, this.settings, metaData);
    }

    @Override
    public org.hibernate.cache.spi.CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metaData) throws CacheException {
        return new CollectionRegion(regionName, this.cacheManager.getCache(regionName), properties, this.settings, metaData);
    }

    @Override
    public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
        return new DefaultQueryResultRegion(regionName, this.cacheManager.getCache(regionName), properties, this.settings);
    }

    @Override
    public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
        return new DefaultTimestampRegion(regionName, this.cacheManager.getCache(regionName), properties, this.settings);
    }

}

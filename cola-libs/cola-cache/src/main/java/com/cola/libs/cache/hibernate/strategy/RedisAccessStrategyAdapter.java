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
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;
import org.jetbrains.annotations.Nullable;
import org.springframework.cache.Cache;

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

    @Nullable
    protected Object get(Object key) throws CacheException {
        return this.cache.get(key);
    }

    protected boolean putFromLoad(Object key, Object val) throws CacheException {
        return putFromLoad(key, val, settings.isMinimalPutsEnabled());
    }

    protected boolean putFromLoad(Object key, Object val, boolean minimalPutOverride) throws CacheException {
        IntensiveCache intensiveCache = (IntensiveCache)this.cache;
        if (minimalPutOverride && intensiveCache.exists(key)) {
            return false;
        } else {
            this.putFromLoad(key, val);
            return true;
        }
    }

    @Nullable
    protected SoftLock lockRegion() throws CacheException {
        return null;
    }

    protected void unlockRegion(SoftLock lock) throws CacheException {
    }

    protected final void removeAll() throws CacheException {
        this.evictAll();
    }

    protected void evictAll() throws CacheException {
        this.cache.clear();
    }

    protected void evict(Object key) throws CacheException {
        this.cache.evict(key);
    }

    @Nullable
    public abstract SoftLock lock(Object var1) throws CacheException;

    public abstract void unlock(Object var1, SoftLock var2) throws CacheException;

    public abstract boolean update(Object var1, Object var2) throws CacheException;

    public abstract boolean afterUpdate(Object var1, Object var2, SoftLock var3) throws CacheException;

    public abstract boolean insert(Object var1, Object var2) throws CacheException;

    public abstract boolean afterInsert(Object var1, Object var2) throws CacheException;

    public abstract void remove(Object var1) throws CacheException;

}

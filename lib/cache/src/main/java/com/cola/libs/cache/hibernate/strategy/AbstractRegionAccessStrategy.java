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

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.RegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.jetbrains.annotations.Nullable;

/**
 * cola
 * Created by jiachen.shi on 8/29/2016.
 */
public class AbstractRegionAccessStrategy implements RegionAccessStrategy {

    protected final AccessStrategyAdapter stgy;

    protected AbstractRegionAccessStrategy(AccessStrategyAdapter stgy) {
        this.stgy = stgy;
    }

    @Override
    public Object get(SharedSessionContractImplementor sharedSessionContractImplementor, Object key, long txTs) throws CacheException {
        return this.stgy.get(key);
    }

    @Override
    public boolean putFromLoad(SharedSessionContractImplementor sharedSessionContractImplementor, Object key, Object val, long txTs, Object ver) throws CacheException {
        return this.stgy.putFromLoad(key, val);
    }

    @Override
    public boolean putFromLoad(SharedSessionContractImplementor sharedSessionContractImplementor, Object key, Object val, long txTs, Object ver, boolean minimalPutOverride) throws CacheException {
        return this.stgy.putFromLoad(key, val, minimalPutOverride);
    }

    @Override
    public SoftLock lockItem(SharedSessionContractImplementor sharedSessionContractImplementor, Object key, Object ver) throws CacheException {
        return this.stgy.lock(key);
    }

    @Nullable
    public SoftLock lockRegion() throws CacheException {
        return this.stgy.lockRegion();
    }

    @Override
    public void unlockItem(SharedSessionContractImplementor sharedSessionContractImplementor, Object key, SoftLock softLock) throws CacheException {
        this.stgy.unlock(key, softLock);
    }

    public void unlockRegion(SoftLock lock) throws CacheException {
        this.stgy.unlockRegion(lock);
    }

    @Override
    public void remove(SharedSessionContractImplementor sharedSessionContractImplementor, Object key) throws CacheException {
        this.stgy.remove(key);
    }

    public void removeAll() throws CacheException {
        this.stgy.removeAll();
    }

    public void evict(Object key) throws CacheException {
        this.stgy.evict(key);
    }

    public void evictAll() throws CacheException {
        this.stgy.evictAll();
    }
}

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

import com.cola.lib.cache.hibernate.strategy.AccessStrategyAdapter;
import com.cola.lib.cache.hibernate.strategy.AbstractRegionAccessStrategy;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.cache.Cache;

import java.util.Properties;

/**
 * cola
 * Created by jiachen.shi on 8/29/2016.
 */
public class DefaultEntityRegion extends AbstractTransactionalDataRegion implements EntityRegion {


    public DefaultEntityRegion(String regionName, Cache cache, Properties properties, SessionFactoryOptions settings, CacheDataDescription metaData) {
        super(regionName, cache, properties, settings, metaData);
    }

    @Override
    public EntityRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return new DefaultEntityRegion.AccessStrategy(this.createAccessStrategy(accessType));
    }

    private class AccessStrategy extends AbstractRegionAccessStrategy implements EntityRegionAccessStrategy {

        private AccessStrategy(AccessStrategyAdapter stgy) {
            super(stgy);
        }

        @Override
        public Object generateCacheKey(Object o, EntityPersister entityPersister, SessionFactoryImplementor sessionFactoryImplementor, String s) {
            return null;
        }

        @Override
        public Object getCacheKeyId(Object o) {
            return null;
        }

        @Override
        public org.hibernate.cache.spi.EntityRegion getRegion() {
            return DefaultEntityRegion.this;
        }

        @Override
        public boolean insert(SharedSessionContractImplementor sharedSessionContractImplementor, Object key, Object val, Object ver) throws CacheException {
            return this.stgy.insert(key, val);
        }

        @Override
        public boolean afterInsert(SharedSessionContractImplementor sharedSessionContractImplementor, Object key, Object val, Object ver) throws CacheException {
            return this.stgy.afterInsert(key, val);
        }

        @Override
        public boolean update(SharedSessionContractImplementor sharedSessionContractImplementor, Object key, Object val, Object currVer, Object previousVer) throws CacheException {
            return this.stgy.update(key, val);
        }

        @Override
        public boolean afterUpdate(SharedSessionContractImplementor sharedSessionContractImplementor, Object key, Object val, Object currVer, Object previousVer, SoftLock softLock) throws CacheException {
            return this.stgy.afterUpdate(key, val, softLock);
        }
    }
}

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
package com.cola.libs.cache.hibernate.region;

import com.cola.libs.cache.hibernate.strategy.AbstractRedisRegionAccessStrategy;
import com.cola.libs.cache.hibernate.strategy.RedisAccessStrategyAdapter;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;

import java.util.Properties;

/**
 * cola
 * Created by jiachen.shi on 8/29/2016.
 */
public class RedisEntityRegion extends AbstractTransactionalDataRegion implements EntityRegion{


    public RedisEntityRegion(String regionName, Cache cache, Properties properties, Settings settings, CacheDataDescription metaData) {
        super(regionName, cache, properties, settings, metaData);
    }

    @Override
    public EntityRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return new RedisEntityRegion.AccessStrategy(this.createAccessStrategy(accessType));
    }

    private class AccessStrategy extends AbstractRedisRegionAccessStrategy implements EntityRegionAccessStrategy {

        private AccessStrategy(RedisAccessStrategyAdapter stgy) {
            super(stgy);
        }

        public boolean insert(Object key, Object val, Object ver) throws CacheException {
            return this.stgy.insert(key, val);
        }

        public boolean afterInsert(Object key, Object val, Object ver) throws CacheException {
            return this.stgy.afterInsert(key, val);
        }

        public boolean update(Object key, Object val, Object currVer, Object previousVer) throws CacheException {
            return this.stgy.update(key, val);
        }

        public boolean afterUpdate(Object key, Object val, Object currVer, Object previousVer, SoftLock lock) throws CacheException {
            return this.stgy.afterUpdate(key, val, lock);
        }

        @Override
        public EntityRegion getRegion() {
            return RedisEntityRegion.this;
        }
    }
}

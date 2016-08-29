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

import com.cola.libs.cache.hibernate.strategy.RedisAccessStrategyAdapter;
import com.cola.libs.cache.hibernate.strategy.RedisReadWriteAccessStrategy;

import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.TransactionalDataRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;

import java.util.Properties;

/**
 * cola
 * Created by jiachen.shi on 8/29/2016.
 */
public class AbstractTransactionalDataRegion extends AbstractRegion implements TransactionalDataRegion {

    private final CacheDataDescription metaData;

    public AbstractTransactionalDataRegion(String regionName, Cache cache, Properties properties, Settings settings, CacheDataDescription metaData) {
        super(regionName, cache, properties, settings);
        this.metaData = metaData;
    }

    @Override
    public boolean isTransactionAware() {
        return false;
    }

    @Override
    public CacheDataDescription getCacheDataDescription() {
        return this.metaData;
    }

    protected RedisAccessStrategyAdapter createAccessStrategy(AccessType accessType) {
        switch(accessType) {
            case READ_ONLY:
                return null;
            case NONSTRICT_READ_WRITE:
                return null;
            case READ_WRITE:
                return new RedisReadWriteAccessStrategy(this.cache, this.settings, metaData);
            case TRANSACTIONAL:
                return null;
            default:
                throw new IllegalArgumentException("Unknown Hibernate access type: " + accessType);
        }
    }


}

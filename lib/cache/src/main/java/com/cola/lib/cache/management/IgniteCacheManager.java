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
package com.cola.lib.cache.management;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.IgniteKernal;
import org.apache.ignite.internal.processors.cache.IgniteInternalCache;
import org.apache.ignite.internal.util.typedef.G;
import org.hibernate.cache.CacheException;
import org.jsr166.ConcurrentHashMap8;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * cola
 * Created by jiachen.shi on 8/24/2016.
 */
public class IgniteCacheManager implements CacheManager, InitializingBean {

    private Ignite ignite;
    private String gridName;
    private IgniteConfiguration cfg;
    private Set<String> cacheNames = new LinkedHashSet(16);
    private final ConcurrentMap<String, CustomizedIgniteCache> caches = new ConcurrentHashMap8();

    public IgniteConfiguration getConfiguration() {
        return this.cfg;
    }

    public void setConfiguration(IgniteConfiguration cfg) {
        this.cfg = cfg;
    }

    public String getGridName() {
        return this.gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public void afterPropertiesSet() throws Exception {
        this.caches.clear();
        this.cacheNames.clear();
        if (this.getConfiguration() != null) {
            this.ignite = G.start(this.getConfiguration());
        } else {
            this.ignite = Ignition.ignite(this.getGridName());
        }
    }

    public Cache getCache(String name) {
        CustomizedIgniteCache customizedIgniteCache = (CustomizedIgniteCache) this.caches.get(name);
        if(customizedIgniteCache == null) {
            IgniteInternalCache cache = ((IgniteKernal)this.ignite).getCache(name);
            if(cache == null) {
                throw new CacheException("Cache \'" + name + "\' is not configured.");
            } else {
                customizedIgniteCache = new CustomizedIgniteCache(this.ignite, cache);
                this.caches.putIfAbsent(name, customizedIgniteCache);
                this.cacheNames.add(name);
            }
        }
        customizedIgniteCache.setTxCtx(new ThreadLocal());
        return customizedIgniteCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheNames);
    }

}

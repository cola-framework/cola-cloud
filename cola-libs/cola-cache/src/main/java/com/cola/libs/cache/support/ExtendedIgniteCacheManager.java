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
package com.cola.libs.cache.support;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteSpring;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.NearCacheConfiguration;
import org.jsr166.ConcurrentHashMap8;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ConcurrentMap;

/**
 * cola
 * Created by jiachen.shi on 8/24/2016.
 */
public class ExtendedIgniteCacheManager extends SpringCacheManager {

    private Ignite ignite;
    private ApplicationContext springCtx;
    private final ConcurrentMap<String, ExtendedIgniteCache> caches = new ConcurrentHashMap8();

    public void setApplicationContext(ApplicationContext ctx) {
        this.springCtx = ctx;
    }

    public void afterPropertiesSet() throws Exception {
        assert this.ignite == null;

        if(super.getConfigurationPath() != null && super.getConfiguration() != null) {
            throw new IllegalArgumentException("Both \'configurationPath\' and \'configuration\' are provided. Set only one of these properties if you need to start a Ignite node inside of SpringCacheManager. If you already have a node running, omit both of them and set\'gridName\' property.");
        } else {
            if(super.getConfigurationPath() != null) {
                this.ignite = IgniteSpring.start(super.getConfigurationPath(), this.springCtx);
            } else if(super.getConfiguration() != null) {
                this.ignite = IgniteSpring.start(super.getConfiguration(), this.springCtx);
            } else {
                this.ignite = Ignition.ignite(super.getGridName());
            }

        }
    }

    public Cache getCache(String name) {
        assert this.ignite != null;

        ExtendedIgniteCache cache = (ExtendedIgniteCache)this.caches.get(name);
        if(cache == null) {
            CacheConfiguration cacheCfg = super.getDynamicCacheConfiguration() != null?new CacheConfiguration(super.getDynamicCacheConfiguration()):new CacheConfiguration();
            NearCacheConfiguration nearCacheCfg = super.getDynamicNearCacheConfiguration() != null?new NearCacheConfiguration(super.getDynamicNearCacheConfiguration()):null;
            cacheCfg.setName(name);
            cache = new ExtendedIgniteCache(nearCacheCfg != null?this.ignite.getOrCreateCache(cacheCfg, nearCacheCfg):this.ignite.getOrCreateCache(cacheCfg));
            ExtendedIgniteCache old = (ExtendedIgniteCache)this.caches.putIfAbsent(name, cache);
            if(old != null) {
                cache = old;
            }
        }

        return cache;
    }

}

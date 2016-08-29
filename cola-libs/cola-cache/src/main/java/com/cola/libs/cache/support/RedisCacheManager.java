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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;

/**
 * cola
 * Created by jiachen.shi on 8/29/2016.
 */
public class RedisCacheManager extends AbstractTransactionSupportingCacheManager {

    private final Log logger = LogFactory.getLog(RedisCacheManager.class);
    private final RedisTemplate template;
    private long expiration;

    public RedisCacheManager(RedisTemplate template, long expiration) {
        this.expiration = expiration;
        this.template = template;
    }

    protected RedisTemplate getTemplate() {
        return this.template;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Assert.notNull(this.template, "A redis template is required in order to interact with data store");
        return Collections.emptyList();
    }

    public Cache getCache(String name) {
        Cache cache = super.getCache(name);
        return cache == null?this.createAndAddCache(name):cache;
    }

    protected Cache createAndAddCache(String cacheName) {
        this.addCache(this.createCache(cacheName));
        return super.getCache(cacheName);
    }

    protected RedisCache createCache(String cacheName) {
        return new RedisCache(cacheName, this.template, this.expiration);
    }

}

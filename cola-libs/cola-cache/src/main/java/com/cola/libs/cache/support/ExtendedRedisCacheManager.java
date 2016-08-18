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

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * cola
 * Created by jiachen.shi on 8/15/2016.
 */
public class ExtendedRedisCacheManager extends RedisCacheManager {

    private Long expiration = 0L;

    public ExtendedRedisCacheManager(RedisTemplate template) {
        super(template, Collections.emptyList());
    }

    public ExtendedRedisCacheManager(RedisTemplate template, Long expiration) {
        super(template, Collections.emptyList());
        if(expiration != null){
            this.expiration = expiration;
        }
    }

    public Cache getCache(String cacheName) {
        if(StringUtils.isEmpty(cacheName)){
            cacheName = "Cache";
        }
        return super.getCache(cacheName);
    }

    protected RedisCache createCache(String cacheName) {
        if(StringUtils.isEmpty(cacheName)){
            cacheName = "Cache";
        }
        return new ExtendedRedisCache(cacheName, super.getTemplate(), this.expiration);
    }

}

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Set;

/**
 * cola
 * Created by jiachen.shi on 8/16/2016.
 */
public class ExtendedRedisCache extends RedisCache implements Cache {

    private static Logger logger = LoggerFactory.getLogger(ExtendedRedisCache.class);
    private final RedisTemplate template;
    private final long expiration;
    private final byte[] cacheLockName;
    private long WAIT_FOR_LOCK = 300L;

    public ExtendedRedisCache(String name, RedisTemplate<? extends Object, ? extends Object> template, long expiration) {
        super(name, null ,template, expiration);
        this.template = template;
        this.expiration = expiration;
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        this.cacheLockName = stringSerializer.serialize(name + "~lock");
    }

    private byte[] convertToBytesIfNecessary(RedisSerializer<Object> serializer, Object value) {
        return serializer == null && value instanceof byte[]?(byte[])((byte[])value):serializer.serialize(value);
    }

    private byte[] computeKey(Object key) {
        byte[] keyBytes = this.convertToBytesIfNecessary(this.template.getKeySerializer(), key);
        return keyBytes;
    }

    private ValueWrapper toWrapper(Object value) {
        return value != null?new SimpleValueWrapper(value):null;
    }

    private Object deserializeIfNecessary(RedisSerializer<byte[]> serializer, byte[] value) {
        return serializer != null?serializer.deserialize(value):value;
    }

    private boolean waitForLock(RedisConnection connection) {
        boolean foundLock = false;

        boolean retry;
        do {
            retry = false;
            if(connection.exists(this.cacheLockName).booleanValue()) {
                foundLock = true;

                try {
                    Thread.sleep(this.WAIT_FOR_LOCK);
                } catch (InterruptedException var5) {
                    Thread.currentThread().interrupt();
                }

                retry = true;
            }
        } while(retry);

        return foundLock;
    }

    @Override
    public ValueWrapper get(Object key) {
        try {
            return super.get(key);
        } catch (Exception e) {
            logger.error("Redis Cache get Method is error:" + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        final byte[] keyBytes = this.computeKey(key);
        final byte[] valueBytes = this.convertToBytesIfNecessary(this.template.getValueSerializer(), value);
        try {
            this.template.execute(new RedisCallback() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    ExtendedRedisCache.this.waitForLock(connection);
                    connection.multi();
                    connection.set(keyBytes, valueBytes);
                    if(ExtendedRedisCache.this.expiration > 0L) {
                        connection.expire(keyBytes, ExtendedRedisCache.this.expiration);
                    }
                    connection.exec();
                    return null;
                }
            }, true);
        } catch (Exception e) {
            logger.error("Redis Cache put Method is error:" + e.getMessage(), e);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        final byte[] keyBytes = this.computeKey(key);
        final byte[] valueBytes = this.convertToBytesIfNecessary(this.template.getValueSerializer(), value);
        try{
            return this.toWrapper(this.template.execute(new RedisCallback() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    ExtendedRedisCache.this.waitForLock(connection);
                    Object resultValue = value;
                    boolean valueWasSet = connection.setNX(keyBytes, valueBytes).booleanValue();
                    if(valueWasSet) {
                        if(ExtendedRedisCache.this.expiration > 0L) {
                            connection.expire(keyBytes, ExtendedRedisCache.this.expiration);
                        }
                    } else {
                        resultValue = ExtendedRedisCache.this.deserializeIfNecessary(ExtendedRedisCache.this.template.getValueSerializer(), connection.get(keyBytes));
                    }
                    return resultValue;
                }
            }, true));
        }catch (Exception e){
            logger.error("Redis Cache putIfAbsent Method is error:" + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void evict(Object key) {
        try{
            final byte[] k = this.computeKey(key);
            this.template.execute(new RedisCallback() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    if(connection.exists(ExtendedRedisCache.this.cacheLockName).booleanValue()) {
                        return null;
                    } else {
                        try {
                            connection.set(ExtendedRedisCache.this.cacheLockName, ExtendedRedisCache.this.cacheLockName);
                            if(ExtendedRedisCache.this.expiration > 0L) {
                                connection.expire(ExtendedRedisCache.this.cacheLockName, ExtendedRedisCache.this.expiration);
                            }

                            Set<byte[]> keys = connection.keys(k);
                            if(keys != null && keys.size() > 0){
                                for(byte[] b:keys){
                                    connection.del(new byte[][]{b});
                                }
                            }
                        } finally {
                            connection.del(new byte[][]{ExtendedRedisCache.this.cacheLockName});
                        }
                    }
                    return null;
                }
            }, true);
        }catch (Exception e){
            logger.error("Redis Cache evict Method is error:" + e.getMessage(), e);
        }
    }

    @Override
    public void clear() {
        try{
            super.clear();
        }catch (Exception e){
            logger.error("Redis Cache clear Method is error:" + e.getMessage(), e);
        }
    }
}

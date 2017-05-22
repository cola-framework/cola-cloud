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
package com.cola.libs.cache.management;

import com.cola.libs.cache.hibernate.IntensiveCache;

import org.hibernate.cache.spi.access.SoftLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * cola
 * Created by jiachen.shi on 8/29/2016.
 */
public class RedisCache implements IntensiveCache {

    private static Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private final String name;
    private final byte[] setName;
    private final RedisTemplate template;
    private final long expiration;

    public RedisCache(String name, RedisTemplate<? extends Object, ? extends Object> template, long expiration) {
        this.name = name;
        this.template = template;
        this.expiration = expiration;
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        this.setName = stringSerializer.serialize(name + "~keys");
    }

    private Object deserializeIfNecessary(RedisSerializer<byte[]> serializer, byte[] value) {
        return serializer != null ? serializer.deserialize(value) : value;
    }

    private ValueWrapper toWrapper(Object value) {
        return value != null ? new SimpleValueWrapper(value) : null;
    }

    private byte[] convertToBytesIfNecessary(RedisSerializer<Object> serializer, Object value) {
        return serializer == null && value instanceof byte[] ? (byte[]) ((byte[]) value) : serializer.serialize(value);
    }

    private byte[] computeKey(Object key) {
        byte[] keyBytes = this.convertToBytesIfNecessary(this.template.getKeySerializer(), key);
        return keyBytes;
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            Boolean result = (Boolean) this.template.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.exists(RedisCache.this.computeKey(key));
                }
            }, true);
            if(result != null){
                return result;
            }
        } catch (Exception e) {
            logger.error("Redis Cache containsKey Method is error:" + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public long size() {
        try {
            Long result = (Long) this.template.execute(new RedisCallback<Long>() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.sCard(RedisCache.this.setName);
                }
            }, true);
            if(result != null){
                return result.longValue();
            }
        } catch (Exception e) {
            logger.error("Redis Cache size Method is error:" + e.getMessage(), e);
        }
        return 0L;
    }

    @Override
    public SoftLock lock(Object key) {
        return null;
    }

    @Override
    public void unlock(Object key, SoftLock lock) {
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.template;
    }

    public ValueWrapper get(Object key) {
        try {
            return (ValueWrapper) this.template.execute(new RedisCallback() {
                public ValueWrapper doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] bs = connection.get(RedisCache.this.computeKey(key));
                    Object value = RedisCache.this.template.getValueSerializer() != null ? RedisCache.this.template.getValueSerializer().deserialize(bs) : bs;
                    return bs == null ? null : new SimpleValueWrapper(value);
                }
            }, true);
        } catch (Exception e) {
            logger.error("Redis Cache get Method is error:" + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = this.get(key);
        return wrapper == null ? null : (T) wrapper.get();
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        final byte[] keyBytes = this.computeKey(key);
        final byte[] valueBytes = this.convertToBytesIfNecessary(this.template.getValueSerializer(), value);
        try {
            this.template.execute(connection -> {
                connection.multi();
                connection.set(keyBytes, valueBytes);
                connection.zAdd(RedisCache.this.setName, 0.0D, keyBytes);
                if (RedisCache.this.expiration > 0L) {
                    connection.expire(keyBytes, RedisCache.this.expiration);
                    connection.expire(RedisCache.this.setName, RedisCache.this.expiration);
                }
                connection.exec();
                return null;
            }, true);
        } catch (Exception e) {
            logger.error("Redis Cache put Method is error:" + e.getMessage(), e);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        final byte[] keyBytes = this.computeKey(key);
        final byte[] valueBytes = this.convertToBytesIfNecessary(this.template.getValueSerializer(), value);
        try {
            return this.toWrapper(this.template.execute(connection -> {
                Object resultValue = value;
                boolean valueWasSet = connection.setNX(keyBytes, valueBytes).booleanValue();
                if (valueWasSet) {
                    connection.zAdd(RedisCache.this.setName, 0.0D, keyBytes);
                    if (RedisCache.this.expiration > 0L) {
                        connection.expire(keyBytes, RedisCache.this.expiration);
                        connection.expire(RedisCache.this.setName, RedisCache.this.expiration);
                    }
                } else {
                    resultValue = RedisCache.this.deserializeIfNecessary(RedisCache.this.template.getValueSerializer(), connection.get(keyBytes));
                }
                return resultValue;
            }, true));
        } catch (Exception e) {
            logger.error("Redis Cache putIfAbsent Method is error:" + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void evict(Object key) {
        try {
            this.template.execute(connection -> {
                List<byte[]> kList = new ArrayList<byte[]>();
                if (key != null) {
                    if (key instanceof Collection) {
                        for (Object k : (Collection) key) {
                            kList.add(RedisCache.this.computeKey(k));
                        }
                    } else {
                        kList.add(RedisCache.this.computeKey(key));
                    }
                }

                for (byte[] k : kList) {
                    Set<byte[]> keys = connection.keys(k);
                    if (keys != null && keys.size() > 0) {
                        for (byte[] b : keys) {
                            connection.del(new byte[][]{b});
                            connection.zRem(RedisCache.this.setName, new byte[][]{b});
                        }
                    }
                }
                return null;
            }, true);
        } catch (Exception e) {
            logger.error("Redis Cache evict Method is error:" + e.getMessage(), e);
        }
    }

    @Override
    public void clear() {
        try {
            this.template.execute(new RedisCallback() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    int offset = 0;
                    boolean finished = false;
                    Set keys;
                    do {
                        keys = connection.zRange(RedisCache.this.setName, (long) (offset * 128), (long) ((offset + 1) * 128 - 1));
                        finished = keys.size() < 128;
                        ++offset;
                        if (!keys.isEmpty()) {
                            connection.del((byte[][]) keys.toArray(new byte[keys.size()][]));
                        }
                    } while (!finished);

                    connection.del(new byte[][]{RedisCache.this.setName});
                    keys = null;
                    return keys;
                }
            }, true);
        } catch (Exception e) {
            logger.error("Redis Cache clear Method is error:" + e.getMessage(), e);
        }
    }
}

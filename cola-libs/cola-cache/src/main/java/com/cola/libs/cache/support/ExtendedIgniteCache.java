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


import org.apache.ignite.IgniteCache;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * cola
 * Created by jiachen.shi on 8/24/2016.
 */
public class ExtendedIgniteCache implements Cache {

    private final IgniteCache<Object, Object> cache;

    ExtendedIgniteCache(IgniteCache<Object, Object> cache) {
        assert cache != null;

        this.cache = cache;
    }

    public String getName() {
        return this.cache.getName();
    }

    public Object getNativeCache() {
        return this.cache;
    }

    public ValueWrapper get(Object key) {
        Object val = this.cache.get(key);
        return val != null?new SimpleValueWrapper(val):null;
    }

    public <T> T get(Object key, Class<T> type) {
        Object val = this.cache.get(key);
        if(val != null && type != null && !type.isInstance(val)) {
            throw new IllegalStateException("Cached value is not of required type [cacheName=" + this.cache.getName() + ", key=" + key + ", val=" + val + ", requiredType=" + type + ']');
        } else {
            return (T)val;
        }
    }

    public void put(Object key, Object val) {
        this.cache.put(key, val);
    }

    public ValueWrapper putIfAbsent(Object key, Object val) {
        Boolean old = Boolean.valueOf(this.cache.putIfAbsent(key, val));
        return old != null?new SimpleValueWrapper(old):null;
    }

    public void evict(Object key) {

        this.cache.remove(key);
    }

    public void clear() {
        this.cache.removeAll();
    }
}

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

import com.cola.libs.cache.hibernate.IntensiveCache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.QueryKey;
import org.hibernate.cache.spi.Region;
import org.springframework.cache.Cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * cola
 * Created by jiachen.shi on 8/29/2016.
 */
public class AbstractRegion implements Region {

    private String regionName;
    protected final Cache cache;
    protected final Properties properties;

    public AbstractRegion(String regionName, Cache cache, Properties properties) {
        this.regionName = regionName;
        this.cache = cache;
        this.properties = properties;
    }

    protected Object getActualKey(Object key){
        if(key instanceof QueryKey){
            return key.toString();
        }
        return key;
    }

    protected Class<?> getReturnClassFromKey(Object key) {
        Class<?> resultClass = null;
        if(key instanceof QueryKey){
            resultClass = ArrayList.class;
        }else{
            resultClass = Object.class;
        }
        return resultClass;
    }

    @Override
    public String getName() {
        return this.regionName;
    }

    @Override
    public void destroy() throws CacheException {
    }

    @Override
    public boolean contains(Object key) {
        if(this.cache instanceof IntensiveCache){
            return ((IntensiveCache)cache).containsKey(getActualKey(key));
        }
        return false;
    }

    @Override
    public long getSizeInMemory() {
        return -1L;
    }

    @Override
    public long getElementCountInMemory() {
        if(this.cache instanceof IntensiveCache){
            return ((IntensiveCache)cache).size();
        }
        return -1L;
    }

    @Override
    public long getElementCountOnDisk() {
        return -1L;
    }

    @Override
    public Map toMap() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public long nextTimestamp() {
        return System.currentTimeMillis();
    }

    @Override
    public int getTimeout() {
        return 0;
    }
}

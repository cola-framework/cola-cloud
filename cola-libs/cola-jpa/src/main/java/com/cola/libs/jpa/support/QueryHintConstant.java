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
package com.cola.libs.jpa.support;

/**
 * cola
 * Created by jiachen.shi on 8/15/2016.
 */
public interface QueryHintConstant {

    public final String FETCH_GRAPH = "javax.persistence.fetchgraph";
    public final String LOAD_GRAPH = "javax.persistence.loadgraph";
    public final String CACHE_RETRIEVE_MODE = "javax.persistence.cache.retrieveMode";
    public final String CACHE_STORE_MODE = "javax.persistence.cache.storeMode";
    public final String QUERY_TIMEOUT = "javax.persistence.query.timeout";
    public final String LOCK_TIMEOUT = "javax.persistence.lock.timeout";
    public final String LOCK_SCOPE = "javax.persistence.lock.scope";
    public final String CACHEABLE = "org.hibernate.cacheable";

}

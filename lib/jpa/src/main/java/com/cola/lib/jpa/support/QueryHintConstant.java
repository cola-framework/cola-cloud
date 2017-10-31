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
package com.cola.lib.jpa.support;

/**
 * cola
 * Created by jiachen.shi on 8/15/2016.
 */
public interface QueryHintConstant {

    /* In this case, All attributes specified in your entity graph will be treated as FetchType.EAGER, and all attributes not specified will be treated as FetchType.LAZY */
    public final String FETCH_GRAPH = "javax.persistence.fetchgraph";

    /* In this case, All attributes specified in the entity graph are also FetchType.EAGER but attributes not specified use their specified type or default if the entity specified nothing. */
    public final String LOAD_GRAPH = "javax.persistence.loadgraph";

    /*
       The cache retrieval mode, set by the javax.persistence.retrieveMode property, controls how data is read from the cache for calls to the EntityManager.find method and from queries
       The retrieveMode property can be set to one of the constants defined by the javax.persistence.CacheRetrieveMode enumerated type, either USE (the default) or BYPASS.
       When it is set to USE, data is retrieved from the second-level cache, if available. If the data is not in the cache, the persistence provider will read it from the database.
       When it is set to BYPASS, the second-level cache is bypassed and a call to the database is made to retrieve the data.
    */
    public final String CACHE_RETRIEVE_MODE = "javax.persistence.cache.retrieveMode";

    /*
       The cache store mode, set by the javax.persistence.storeMode property, controls how data is stored in the cache.
       The storeMode property can be set to one of the constants defined by the javax.persistence.CacheStoreMode enumerated type, either USE (the default), BYPASS, or REFRESH.
       When set to USE the cache data is created or updated when data is read from or committed to the database. If data is already in the cache, setting the store mode to USE will not force a refresh when data is read from the database.
       When the store mode is set to BYPASS, data read from or committed to the database is not inserted or updated in the cache. That is, the cache is unchanged.
       When the store mode is set to REFRESH, the cache data is created or updated when data is read from or committed to the database, and a refresh is forced on data in the cache upon database reads.
    */
    public final String CACHE_STORE_MODE = "javax.persistence.cache.storeMode";


    public final String QUERY_TIMEOUT = "javax.persistence.query.timeout";
    public final String LOCK_TIMEOUT = "javax.persistence.lock.timeout";
    public final String LOCK_SCOPE = "javax.persistence.lock.scope";

    /*
       ALL - All entities and entity-related state and data are cached.
       NONE - Caching is disabled for the persistence unit.
       ENABLE_SELECTIVE - Caching is enabled for all entities for Cacheable(true) is specified. All other entities are not cached.
       DISABLE_SELECTIVE - Caching is enabled for all entities except those for which Cacheable(false) is specified. Entities for which Cacheable(false) is specified are not cached.
       UNSPECIFIED - Caching behavior is undefined: provider-specific defaults may apply.
     */
    public final String SHARED_CACHE_MODE="javax.persistence.sharedCache.mode";
    public final String CACHEABLE = "org.hibernate.cacheable";

}

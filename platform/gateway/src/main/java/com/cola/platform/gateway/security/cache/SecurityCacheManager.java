package com.cola.platform.gateway.security.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by jiachen.shi on 8/1/2017.
 */
public class SecurityCacheManager implements CacheManager {

    private final Log logger = LogFactory.getLog(SecurityCacheManager.class);
    private final RedisTemplate template;
    private long expiration;

    public SecurityCacheManager(RedisTemplate template, long expiration) {
        this.expiration = expiration;
        this.template = template;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new SecurityCache<K, V>(name, this.template, this.expiration);
    }
}

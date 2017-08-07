package com.cola.platform.gateway.security.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiachen.shi on 8/1/2017.
 */
public class SecurityCache<K, V> implements Cache<K, V> {

    private final String SECURITY_CACHE = "securityCache:";
    private String cacheKey;
    private RedisTemplate<K, V> redisTemplate;
    private long expiration;

    public SecurityCache(String name, RedisTemplate template, long expiration) {
        this.cacheKey = SECURITY_CACHE + name + ":";
        this.redisTemplate = template;
        this.expiration = expiration;
    }

    private K getCacheKey(Object k) {
        return (K) (this.cacheKey + k);
    }

    @Override
    public V get(K k) throws CacheException {
        redisTemplate.boundValueOps(getCacheKey(k)).expire(expiration, TimeUnit.MINUTES);
        return redisTemplate.boundValueOps(getCacheKey(k)).get();
    }

    @Override
    public V put(K k, V v) throws CacheException {
        V old = get(k);
        redisTemplate.boundValueOps(getCacheKey(k)).set(v);
        return old;
    }

    @Override
    public V remove(K k) throws CacheException {
        V old = get(k);
        redisTemplate.delete(getCacheKey(k));
        return old;
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.delete(keys());
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        return redisTemplate.keys(getCacheKey("*"));
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<>();
        for (K s : set) {
            list.add(get(s));
        }
        return list;
    }
}

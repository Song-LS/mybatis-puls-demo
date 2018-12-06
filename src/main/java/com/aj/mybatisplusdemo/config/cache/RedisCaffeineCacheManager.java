package com.aj.mybatisplusdemo.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author colin
 * @date 2018-12-06
 * redis和caffeine缓存管理
 **/
@Slf4j
public class RedisCaffeineCacheManager implements CacheManager {
    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private CacheRedisCaffeineProperties cacheRedisCaffeineProperties;

    private RedisTemplate<Object, Object> redisTemplate;

    private boolean dynamic;

    private Set<String> cacheNames;

    RedisCaffeineCacheManager(CacheRedisCaffeineProperties cacheRedisCaffeineProperties,
                              RedisTemplate<Object, Object> redisTemplate) {
        super();
        this.cacheRedisCaffeineProperties = cacheRedisCaffeineProperties;
        this.redisTemplate = redisTemplate;
        this.dynamic = cacheRedisCaffeineProperties.isDynamic();
        this.cacheNames = cacheRedisCaffeineProperties.getCacheNames();
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if(cache != null) {
            return cache;
        }
        if(!dynamic && !cacheNames.contains(name)) {
            return null;
        }
        cache = new RedisCaffeineCache(name, redisTemplate, caffeineCache(), cacheRedisCaffeineProperties);
        Cache oldCache = cacheMap.putIfAbsent(name, cache);
        log.info("创建缓存的实例,缓存名为 : {}", name);
        return oldCache == null ? cache : oldCache;
    }

    /**
     * 构建caffeine缓存
     * @return caffeine cache
     */
    private com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache(){
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if(cacheRedisCaffeineProperties.getCaffeine().getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(cacheRedisCaffeineProperties.getCaffeine().getExpireAfterAccess(), TimeUnit.MILLISECONDS);
        }
        if(cacheRedisCaffeineProperties.getCaffeine().getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(cacheRedisCaffeineProperties.getCaffeine().getExpireAfterWrite(), TimeUnit.MILLISECONDS);
        }
        if(cacheRedisCaffeineProperties.getCaffeine().getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(cacheRedisCaffeineProperties.getCaffeine().getInitialCapacity());
        }
        if(cacheRedisCaffeineProperties.getCaffeine().getMaximumSize() > 0) {
            cacheBuilder.maximumSize(cacheRedisCaffeineProperties.getCaffeine().getMaximumSize());
        }
        if(cacheRedisCaffeineProperties.getCaffeine().getRefreshAfterWrite() > 0) {
            cacheBuilder.refreshAfterWrite(cacheRedisCaffeineProperties.getCaffeine().getRefreshAfterWrite(), TimeUnit.MILLISECONDS);
        }
        return cacheBuilder.build();
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheNames;
    }

    void clearLocal(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if(cache == null) {
            return ;
        }
        RedisCaffeineCache redisCaffeineCache = (RedisCaffeineCache) cache;
        redisCaffeineCache.clearLocal(key);
    }
}

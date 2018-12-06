package com.aj.mybatisplusdemo.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
/**
 * @author colin
 * @date 2018-12-06
 **/
@Slf4j
public class RedisCaffeineCache extends AbstractValueAdaptingCache {

    private String name;

    private RedisTemplate<Object, Object> redisTemplate;

    private Cache<Object, Object> caffeineCache;

    private String cachePrefix;

    private long defaultExpiration = 0;

    private Map<String, Long> expires;

    private String topic = "cache:redis:caffeine:topic";

    protected RedisCaffeineCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    RedisCaffeineCache(String name, RedisTemplate<Object, Object> redisTemplate, Cache<Object, Object> caffeineCache, CacheRedisCaffeineProperties cacheRedisCaffeineProperties) {
        super(cacheRedisCaffeineProperties.isCacheNullValues());
        this.name = name;
        this.redisTemplate = redisTemplate;
        this.caffeineCache = caffeineCache;
        this.cachePrefix = cacheRedisCaffeineProperties.getCachePrefix();
        this.defaultExpiration = cacheRedisCaffeineProperties.getRedis().getDefaultExpiration();
        this.expires = cacheRedisCaffeineProperties.getRedis().getExpires();
        this.topic = cacheRedisCaffeineProperties.getRedis().getTopic();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = lookup(key);
        if(value != null) {
            return (T) value;
        }
        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
            value = lookup(key);
            if(value != null) {
                return (T) value;
            }
            value = valueLoader.call();
            Object storeValue = toStoreValue(valueLoader.call());
            put(key, storeValue);
            return (T) value;
        } catch (Exception e) {
            try {
                Class<?> c = Class.forName("org.springframework.cache.Cache$ValueRetrievalException");
                Constructor<?> constructor = c.getConstructor(Object.class, Callable.class, Throwable.class);
                throw (RuntimeException) constructor.newInstance(key, valueLoader, e.getCause());
            } catch (Exception e1) {
                throw new IllegalStateException(e1);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(Object key, Object value) {
        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
            return;
        }
        redisSet(key, value);
        caffeineCache.put(key, value);
    }

    /**
     * redis插入值
     * @param key key
     * @param value value
     */
    private void redisSet(Object key, Object value) {
        long expire = getExpire();
        if(expire > 0) {
            redisTemplate.opsForValue().set(getKey(key), toStoreValue(value), expire, TimeUnit.MILLISECONDS);
        } else {
            redisTemplate.opsForValue().set(getKey(key), toStoreValue(value));
        }
        push(new CacheMessage(this.name, key));
    }

    /**
     * 判断是否存在,不存在则插入
     * @param key key
     * @param value value
     * @return ValueWrapper
     */
    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object cacheKey = getKey(key);
        Object prevValue;
        // 考虑使用分布式锁，或者将redis的setIfAbsent改为原子性操作
        synchronized (key) {
            prevValue = redisTemplate.opsForValue().get(cacheKey);
            if(prevValue == null) {
                redisSet(key, value);

                caffeineCache.put(key, toStoreValue(value));
            }
        }
        return toValueWrapper(prevValue);
    }

    /**
     * 清除缓存
     * @param key 缓存key
     */
    @Override
    public void evict(Object key) {
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        redisTemplate.delete(getKey(key));
        push(new CacheMessage(this.name, key));
        caffeineCache.invalidate(key);
    }

    /**
     * 清除所有缓存
     */
    @Override
    public void clear() {
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        Set<Object> keys = redisTemplate.keys(this.name);
        assert keys != null;
        for(Object key : keys) {
            redisTemplate.delete(key);
        }

        push(new CacheMessage(this.name, null));

        caffeineCache.invalidateAll();
    }

    @Override
    protected Object lookup(Object key) {
        Object cacheKey = getKey(key);
        Object value = caffeineCache.getIfPresent(key);
        if(value != null) {
            log.info("获取到本地缓存,缓存的键为:{}", cacheKey);
            return value;
        }
        value = redisTemplate.opsForValue().get(cacheKey);
        if(value != null) {
            log.info("从redis里拿到数据,存放到caffeine中,缓存key为:{}", cacheKey.toString());
            caffeineCache.put(key, value);
        }
        return value;
    }

    private Object getKey(Object key) {
        return key.toString();
    }

    /**
     * 拿到过期时间
     * @return 过期时间
     */
    private long getExpire() {
        long expire = defaultExpiration;
        Long cacheNameExpire = expires.get(this.name);
        return cacheNameExpire == null ? expire : cacheNameExpire;
    }

    /**
     * @description 缓存变更时通知其他节点清理本地缓存
     * @param message 消息内容
     */
    private void push(CacheMessage message) {
        redisTemplate.convertAndSend(topic, message);
    }

    /**
     * @description 清理本地(caffeine)缓存
     * @param key 缓存键
     */
    void clearLocal(Object key) {
        if(key == null) {
            caffeineCache.invalidateAll();
        } else {
            caffeineCache.invalidate(key);
        }
    }
}

package com.aj.mybatisplusdemo.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author colin
 * @date 2018-12-06
 * redis消息监听器
 **/
@Slf4j
public class CacheMessageListener implements MessageListener {
    private RedisTemplate<Object, Object> redisTemplate;

    private RedisCaffeineCacheManager redisCaffeineCacheManager;

    CacheMessageListener(RedisTemplate<Object, Object> redisTemplate, RedisCaffeineCacheManager redisCaffeineCacheManager) {
        super();
        this.redisTemplate = redisTemplate;
        this.redisCaffeineCacheManager = redisCaffeineCacheManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        assert cacheMessage != null;
        log.info("收到redis传来的消息,清除本地缓存,缓存名为 {}", cacheMessage.getCacheName(), cacheMessage.getKey());
        redisCaffeineCacheManager.clearLocal(cacheMessage.getCacheName(), cacheMessage.getKey());
    }
}

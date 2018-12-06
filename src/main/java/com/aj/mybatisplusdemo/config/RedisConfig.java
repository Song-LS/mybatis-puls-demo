package com.aj.mybatisplusdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @author colin
 * @date 2018-12-06
 * 解决存入redis乱码问题
 **/
@Configuration
public class RedisConfig {
    @Resource
    private RedisTemplate redisTemplate;

    @Bean
    @SuppressWarnings("unchecked")
    public RedisTemplate redisTemplateInit() {
        //设置序列化Key的实例化对象
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的实例化对象
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}

package com.plant.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration //配置注解 也会放在容器 和Component一样
@Slf4j
public class RedisConfiguration {
    @Bean  //把方法的返回值放到Spring IOC容器里
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置Redis的key使用字符串序列化器，解决key可读性差的问题。
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 设置Redis的value使用JSON序列化器，解决value可读性差的问题。
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }
}
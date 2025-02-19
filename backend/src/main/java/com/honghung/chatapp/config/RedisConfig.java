package com.honghung.chatapp.config;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
- This class is used to configure Redis connection and template in a Spring-based application.
- It sets up the connection to a Redis server using the host and port specified in the application properties,
and creates a RedisTemplate with custom serializers for keys, values, and hash keys to support JSON serialization
and deserialization, including support for Java 8 date and time types. 
* Some implementations of RedisSerializer:
1. StringRedisSerializer:  used for serializing and deserializing String values.
2. Jackson2JsonRedisSerializer:  serializes objects to and from JSON format using the Jackson library.
3. GenericJackson2JsonRedisSerializer:  a generic version of Jackson2JsonRedisSerializer that can handle any type of object. 
    Such as complex, nested, or generic types (e.g., List<T>, Map<String, T>, etc.)
4. ...
*/

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}") private String redisHost;
    @Value("${spring.data.redis.port}") private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        var redisStandaloneConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisStandaloneConfig);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        var redisTemplate = new RedisTemplate<Object, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(StringRedisSerializer.UTF_8);
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // to support serializing LocalDateTime, LocalDate
        // Thêm hỗ trợ cho java.util.Date
        SimpleModule dateModule = new SimpleModule();
        dateModule.addSerializer(Date.class, new com.fasterxml.jackson.databind.ser.std.DateSerializer());
        dateModule.addDeserializer(Date.class, new com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer());
        objectMapper.registerModule(dateModule);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return redisTemplate;
    }

    @Bean
        public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
            RedisCacheConfiguration defaults = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))
            .disableCachingNullValues();
            return RedisCacheManager.builder(connectionFactory).cacheDefaults(defaults).build();
        }
}
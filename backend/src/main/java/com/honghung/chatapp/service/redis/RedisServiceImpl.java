package com.honghung.chatapp.service.redis;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service; 

import lombok.RequiredArgsConstructor;

@Primary
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long appendList(String key, Object... values) {
    return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public Long prependList(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    @Override
    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);    
    }

    @Override
    public Long appendSet(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Boolean appendZSet(String key, Object object, double score) {
        return  redisTemplate.opsForZSet().add(key, object, score);
    }

    @Override
    public Set<Object> getZSet(String key) {
        return redisTemplate.opsForZSet().range(key, 0, -1);
    }

    @Override
    public Double increaseScore(String key, Object object, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, object, score);
    }

    @Override
    public Double getScoreFromZSet(String key, Object object) {
        return redisTemplate.opsForZSet().score(key, object);
    }

    @Override
    public void appendHash(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Object getHashWithKeyAndHashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Map<Object, Object> getHashWithKey(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Long deleteHashKey(String key, String... hashKeys) {
        // Convert String[] to Object[]
        Object hashKeyObjs[] = Arrays.asList(hashKeys).stream().map(hashKey -> (Object) hashKey).toArray();
        return redisTemplate.opsForHash().delete(key, hashKeyObjs);
    }

    @Override
    public Long deleteHashKey(String key, List<String> hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    @Override
    public Boolean existsByKey(String key) {
        return  redisTemplate.hasKey(key);
    }

    @Override
    public Boolean existsByHashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Boolean setTimeToLive(String key, long time, TimeUnit timeUnit) {
        return redisTemplate.expire(key, time, timeUnit);
    }
    
    @Override
    public Boolean setTimeToLive(String key, Date timeout) {
        return redisTemplate.expireAt(key, timeout);
    }
    
    @Override
    public Boolean setTimeToLive(String key, Duration timeout) {
        return redisTemplate.expire(key, timeout);
    }

    @Override
    public void flushAll() {
        try (var connection = redisTemplate.getConnectionFactory().getConnection()) {
            connection.serverCommands().flushAll();
        } catch (Exception e) {
            throw new RuntimeException("Cannot flush all keys in redis", e);
        }
    }
}
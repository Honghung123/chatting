package com.honghung.chatapp.service.redis;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service; 

@Service
public interface RedisService {
    void setValue(String key, Object value);

    Object getValue(String key);

    Long appendList(String key, Object... values);

    Long prependList(String key, Object... values);

    List<Object> getList(String key);

    Long appendSet(String key, Object... values);

    Set<Object> getSet(String key);

    Boolean appendZSet(String key, Object object, double score);

    Set<Object> getZSet(String key);

    Double increaseScore(String key, Object object, double score);

    Double getScoreFromZSet(String key, Object object);

    void appendHash(String key, String hashKey, Object value);

    Object getHashWithKeyAndHashKey(String key, String hashKey);

    Map<Object, Object> getHashWithKey(String key);

    Boolean deleteKey(String key);

    Long deleteHashKey(String key, String... hashKeys);

    Long deleteHashKey(String key, List<String> hashKeys);

    Boolean existsByKey(String key);

    Boolean existsByHashKey(String key, String hashKey);

    Boolean setTimeToLive(String key, long timeoutInDays, TimeUnit timeUnit);

    Boolean setTimeToLive(String key, Date timeout);

    Boolean setTimeToLive(String key, Duration timeout);

    void flushAll();
}
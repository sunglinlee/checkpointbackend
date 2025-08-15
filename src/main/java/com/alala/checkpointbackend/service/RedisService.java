package com.alala.checkpointbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, String value) {
        // 使用 String 類型，並設定過期時間為 1 小時
        redisTemplate.opsForValue().set(key, value, 1, java.util.concurrent.TimeUnit.HOURS);
    }

    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}

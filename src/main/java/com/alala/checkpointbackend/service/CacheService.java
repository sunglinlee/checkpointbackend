package com.alala.checkpointbackend.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {
    // 使用 ConcurrentHashMap 儲存緩存資料
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    // 內部類別，用來儲存值和過期時間
    private static class CacheEntry {
        @Getter
        private final Object value;
        private final long expirationTime;

        public CacheEntry(Object value, long ttlInSeconds) {
            this.value = value;
            this.expirationTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(ttlInSeconds);
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }

    /**
     * 將資料放入緩存，並設定過期時間 (TTL)
     *
     * @param key     緩存的鍵
     * @param value   要緩存的值
     * @param ttlInSeconds  過期時間（單位：秒）
     */
    public void put(String key, Object value, long ttlInSeconds) {
        if (key != null && value != null) {
            cache.put(key, new CacheEntry(value, ttlInSeconds));
        }
    }

    /**
     * 從緩存中取得資料
     *
     * @param key 緩存的鍵
     * @return 緩存的值，如果鍵不存在或已過期則返回 null
     */
    public Object get(String key) {
        if (key == null) {
            return null;
        }

        CacheEntry entry = cache.get(key);
        if (entry != null) {
            // 檢查是否過期
            if (entry.isExpired()) {
                // 如果已過期，則從緩存中移除
                cache.remove(key);
                return null;
            }
            return entry.getValue();
        }
        return null;
    }

    /**
     * 從緩存中移除資料
     *
     * @param key 緩存的鍵
     */
    public void remove(String key) {
        if (key != null) {
            cache.remove(key);
        }
    }

    /**
     * 清空所有緩存
     */
    public void clear() {
        cache.clear();
    }
}

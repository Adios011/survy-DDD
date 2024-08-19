package org.survy.infrastructure.cache.listener;

import org.springframework.stereotype.Component;
import org.survy.infrastructure.cache.adapter.RedisCacheService;

@Component
public class RedisKeyExpirationListener {

    private final RedisCacheService redisCacheService;

    public RedisKeyExpirationListener(RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }

    public void handleMessage(String key){
        redisCacheService.delete(key);
    }
}

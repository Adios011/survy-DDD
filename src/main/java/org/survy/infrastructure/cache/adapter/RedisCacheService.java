package org.survy.infrastructure.cache.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.survy.domain.applicationservice.ports.output.cache.CacheService;
import org.survy.domain.core.entity.Survey;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheService<T> implements CacheService<T> {

    private static final long CACHE_DURATION = 2L;

    private final RedisTemplate<String, T> redisTemplate;

    public RedisCacheService(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String key, T object) {
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        ops.set(key, object, CACHE_DURATION, TimeUnit.MINUTES);
    }

    @Override
    public void save(String key, T object, long timeout , TimeUnit timeUnit) {
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        ops.set(key, object , timeout , timeUnit);

    }

    @Override
    public T get(String key) {
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    @Override
    public void delete(String key) {
        ValueOperations<String,T> ops = redisTemplate.opsForValue();
        ops.getAndDelete(key);

    }
}

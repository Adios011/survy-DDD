package org.survy.infrastructure.cache.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.entity.Survey;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class Receiver {

    @Autowired
    private SurveyRepository surveyRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    public Receiver(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private Map<String, Object> map = new ConcurrentHashMap<>();

    private AtomicInteger counter = new AtomicInteger();

    public void receiveMessage(String message) {
        System.out.println("Message has been received -> " + message);
    }

    //    @Scheduled(fixedRate = 2000)
    public void checkKeys() {
        System.out.println("I am checking keys **");
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null)
            return;
        for (String key : keys) {
            Long ttl = redisTemplate.getExpire(key);
            if (ttl != null && ttl < 10) {

                Object value = redisTemplate.opsForValue().get(key);
                map.put(key, value);
            }
        }
    }

    public int getCount() {
        return counter.get();
    }
}
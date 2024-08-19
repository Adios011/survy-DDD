package org.survy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.survy.dataaccess.user.entity.MyUserEntity;
import org.survy.dataaccess.user.repository.UserJpaRepository;

import java.util.UUID;

@Component
public class InitialSetUp {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private RedisTemplate<?,?> redisTemplate;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent applicationReadyEvent){

        MyUserEntity myUserEntity = MyUserEntity.builder()
                .id(UUID.fromString("6b6c07b5-aac8-44f8-8df3-2bca7652f0b6"))
                .name("Muhammet")
                .email("email@gmail.com")
                .build();

        userJpaRepository.save(myUserEntity);


    }



}

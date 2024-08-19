package org.survy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.survy.domain.core.domainService.ParticipationDomainService;
import org.survy.domain.core.domainService.SurveyDomainService;
import org.survy.domain.core.domainService.impl.ParticipationDomainServiceImpl;
import org.survy.domain.core.domainService.impl.SurveyDomainServiceImpl;
import org.survy.domain.core.event.DomainEventPublisher;
import org.survy.domain.core.event.SurveyClosedEvent;
import org.survy.domain.applicationservice.event.SurveyClosedEventHandler;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

//        ParticipationLogic domain = new SimpleQuotaLogic(UUID.randomUUID(), new SurveyId(UUID.randomUUID()),10 , new QuestionId(UUID.randomUUID()) , new HashMap<>());
//        ParticipationLogicEntity jpa = ParticipationLogicDataAccessMapper2.INSTANCE.toParticipationLogicEntity(domain   );
//        System.out.println(jpa.getClass());
//        SimpleQuotaLogicEntity logic = (SimpleQuotaLogicEntity)   jpa;
//        System.out.println(logic);

    }


    @Bean
    public SurveyDomainService surveyDomainService() {
        return new SurveyDomainServiceImpl();
    }

    @Bean
    public ParticipationDomainService participationDomainService() {
        return new ParticipationDomainServiceImpl();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }



    @Bean
    public SurveyClosedEventHandler surveyClosedEventHandler(){
        SurveyClosedEventHandler surveyClosedEventHandler = new SurveyClosedEventHandler();
        DomainEventPublisher.getInstance().register(SurveyClosedEvent.class , surveyClosedEventHandler);
        return surveyClosedEventHandler;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(6);
        threadPoolTaskExecutor.setQueueCapacity(5);
        return threadPoolTaskExecutor;
    }




}

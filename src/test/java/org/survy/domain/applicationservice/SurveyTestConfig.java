package org.survy.domain.applicationservice;

import net.bytebuddy.utility.nullability.MaybeNull;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.survy.domain.applicationservice.mapper.QuestionDataMapper;
import org.survy.domain.applicationservice.mapper.SurveyDataMapper;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.applicationservice.ports.output.repository.UserRepository;
import org.survy.domain.core.domainService.SurveyDomainService;
import org.survy.domain.core.domainService.impl.SurveyDomainServiceImpl;

@SpringBootApplication(scanBasePackages = "org.survy.domain")
@EnableCaching
public class SurveyTestConfig {

    @Bean
    public SurveyRepository surveyRepository() {
        return Mockito.mock(SurveyRepository.class);
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public SurveyDomainService surveyDomainService() {
        return new SurveyDomainServiceImpl();
    }

    @Bean
    public SurveyDataMapper surveyDataMapper() {
        return new SurveyDataMapper(questionDataMapper());
    }



    @Bean
    public QuestionDataMapper questionDataMapper() {
        return new QuestionDataMapper();
    }

    @Bean
    public CacheManager cacheManager(){
        return new ConcurrentMapCacheManager();
    }
}

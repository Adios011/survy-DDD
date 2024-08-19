package org.survy.domain.applicationservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.event.DomainEventHandler;
import org.survy.domain.core.event.SurveyClosedEvent;
import org.survy.domain.core.valueObject.SurveyStatus;

@Slf4j
public class SurveyClosedEventHandler extends DomainEventHandler<SurveyClosedEvent> {

    @Autowired
    private SurveyRepository surveyRepository;


    @Override
    public void handle(SurveyClosedEvent event) {
        System.out.println(Thread.currentThread());
        Thread thread = new Thread(() -> {
            surveyRepository.updateStatus(event.getSurveyId(), SurveyStatus.CLOSED, event.getReason());
            log.info("Survey '{}' is closed for reason: '{}' ", event.getSurveyId().getValue(), event.getReason());
        });
        System.out.println(thread);
        thread.start();



    }
}

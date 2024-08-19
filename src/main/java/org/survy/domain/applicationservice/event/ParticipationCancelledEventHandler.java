package org.survy.domain.applicationservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.survy.domain.applicationservice.ports.output.repository.ParticipationRepository;
import org.survy.domain.core.event.DomainEventHandler;
import org.survy.domain.core.event.ParticipationCancelledEvent;
import org.survy.domain.core.valueObject.ParticipationStatus;

@Slf4j
@Component
public class ParticipationCancelledEventHandler extends DomainEventHandler<ParticipationCancelledEvent> {

    @Autowired
    public ParticipationRepository participationRepository;

    @Override
    @TransactionalEventListener(classes = ParticipationCancelledEvent.class , phase = TransactionPhase.AFTER_COMPLETION)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle( ParticipationCancelledEvent event) {
        participationRepository.updateStatus(event.getParticipationId(), ParticipationStatus.CANCELED);
        log.info("participation '{}' has been cancelled for reason: '{}'", event.getParticipationId().getValue(), event.getReason());
    }
}

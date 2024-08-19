package org.survy.domain.core.event;

import org.springframework.context.ApplicationEvent;
import org.survy.domain.core.valueObject.ParticipationId;

public class ParticipationCancelledEvent extends DomainEvent  {

    private final ParticipationId participationId;
    private final String reason;

    public ParticipationCancelledEvent(ParticipationId participationId, String reason) {
        this.participationId = participationId;
        this.reason = reason;
    }

    public ParticipationId getParticipationId() {
        return participationId;
    }

    public String getReason() {
        return reason;
    }
}

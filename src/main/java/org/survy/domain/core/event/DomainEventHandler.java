package org.survy.domain.core.event;

import org.springframework.scheduling.annotation.Async;

public abstract class DomainEventHandler<T extends DomainEvent> {


    public abstract  void handle(T event);

}

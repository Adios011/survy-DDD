package org.survy.domain.core.event;


import java.util.*;

public class DomainEventPublisher {

    private static DomainEventPublisher instance;

    private final Map<Class<? extends DomainEvent>, List<DomainEventHandler<? extends DomainEvent>>> handlers = new HashMap<>();

    private DomainEventPublisher() {

    }


    public static DomainEventPublisher getInstance() {
        if (instance == null)
            return instance = new DomainEventPublisher();

        return instance;
    }


    public <T extends DomainEvent> void register(Class<T> eventType, DomainEventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }


    public void publish(DomainEvent domainEvent) {
        List<DomainEventHandler<?>> eventHandlers = handlers.get(domainEvent.getClass());
        if (eventHandlers != null)
            for (DomainEventHandler<?> eventHandler : eventHandlers) {
                handleEvent(domainEvent, eventHandler);
            }
    }


    @SuppressWarnings("unchecked")
    private <T extends DomainEvent> void handleEvent(DomainEvent domainEvent, DomainEventHandler<T> domainEventHandler) {
        domainEventHandler.handle((T) domainEvent);
    }


}

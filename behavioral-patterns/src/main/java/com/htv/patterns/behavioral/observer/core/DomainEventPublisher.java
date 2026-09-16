package com.htv.patterns.behavioral.observer.core;

public interface DomainEventPublisher {

    EventListenerSubscription subscribe(
            DomainEventListener<? extends DomainEvent> listener
    );

    EventPublicationResult publish(
            DomainEvent event
    );

    int listenerCount();
}
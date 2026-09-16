package com.htv.patterns.behavioral.observer.core;

public interface DomainEventListener<E extends DomainEvent> {

    Class<E> eventType();

    void onEvent(E event);

    default String listenerName() {
        return getClass().getSimpleName();
    }
}
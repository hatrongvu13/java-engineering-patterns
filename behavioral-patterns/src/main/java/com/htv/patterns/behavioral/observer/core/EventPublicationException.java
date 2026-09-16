package com.htv.patterns.behavioral.observer.core;

public final class EventPublicationException
        extends RuntimeException {

    private final String eventType;
    private final String listenerName;

    public EventPublicationException(
            String eventType,
            String listenerName,
            Throwable cause
    ) {
        super(
                "Listener "
                        + listenerName
                        + " failed while handling event "
                        + eventType,
                cause
        );

        this.eventType = eventType;
        this.listenerName = listenerName;
    }

    public String eventType() {
        return eventType;
    }

    public String listenerName() {
        return listenerName;
    }
}
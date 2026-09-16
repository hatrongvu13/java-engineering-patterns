package com.htv.patterns.behavioral.observer.core;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {

    UUID eventId();

    Instant occurredAt();

    String aggregateId();

    String eventType();
}
package com.htv.patterns.distributed.outbox;

import java.time.Instant;
import java.util.UUID;

public record OutboxEvent(UUID id, String type, String payload, Instant occurredAt) {
    public static OutboxEvent of(String type, String payload) {
        return new OutboxEvent(UUID.randomUUID(), type, payload, Instant.now());
    }
}
package com.htv.patterns.integration.core;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record Message<T>(String id, T payload, Map<String, Object> headers, Instant createdAt) {
    public Message {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(headers, "headers");
        Objects.requireNonNull(createdAt, "createdAt");
        headers = Map.copyOf(headers);
    }

    public static <T> Message<T> of(T payload) {
        return new Message<>(UUID.randomUUID().toString(), payload, Map.of(), Instant.now());
    }

    public Message<T> withHeader(String key, Object value) {
        var copy = new java.util.HashMap<>(headers);
        copy.put(key, value);
        return new Message<>(id, payload, copy, createdAt);
    }

    public <R> Message<R> mapPayload(R newPayload) {
        return new Message<>(id, newPayload, headers, createdAt);
    }
}

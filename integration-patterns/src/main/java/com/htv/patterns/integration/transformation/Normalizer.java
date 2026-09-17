package com.htv.patterns.integration.transformation;

import com.htv.patterns.integration.core.Message;

import java.util.Map;
import java.util.function.Function;

public final class Normalizer<R> {
    private final Map<Class<?>, Function<Object, R>> adapters;

    public Normalizer(Map<Class<?>, Function<Object, R>> adapters) {
        this.adapters = Map.copyOf(adapters);
    }

    public Message<R> normalize(Message<?> message) {
        var adapter = adapters.get(message.payload().getClass());
        if (adapter == null)
            throw new IllegalArgumentException("Unsupported payload: " + message.payload().getClass().getName());
        return new Message<>(message.id(), adapter.apply(message.payload()), message.headers(), message.createdAt());
    }
}

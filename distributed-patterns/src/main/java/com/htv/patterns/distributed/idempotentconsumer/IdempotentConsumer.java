package com.htv.patterns.distributed.idempotentconsumer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class IdempotentConsumer<T> {
    private final Set<String> processed = ConcurrentHashMap.newKeySet();

    public boolean accept(String id, T value, Consumer<T> handler) {
        if (!processed.add(id)) return false;
        try {
            handler.accept(value);
            return true;
        } catch (RuntimeException e) {
            processed.remove(id);
            throw e;
        }
    }
}

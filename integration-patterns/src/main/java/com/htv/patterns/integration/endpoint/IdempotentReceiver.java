package com.htv.patterns.integration.endpoint;

import com.htv.patterns.integration.core.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class IdempotentReceiver<T> implements MessageHandler<T> {
    private final Set<String> processed = ConcurrentHashMap.newKeySet();
    private final MessageHandler<T> delegate;

    public IdempotentReceiver(MessageHandler<T> delegate) {
        this.delegate = delegate;
    }

    public void handle(Message<T> message) {
        if (processed.add(message.id())) delegate.handle(message);
    }

    public int processedCount() {
        return processed.size();
    }
}

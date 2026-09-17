package com.htv.patterns.integration.channel;

import com.htv.patterns.integration.core.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PublishSubscribeChannel<T> implements MessageChannel<T> {
    private final List<MessageHandler<T>> subscribers = new CopyOnWriteArrayList<>();

    public AutoCloseable subscribe(MessageHandler<T> handler) {
        subscribers.add(handler);
        return () -> subscribers.remove(handler);
    }

    public void send(Message<T> message) {
        subscribers.forEach(s -> s.handle(message));
    }

    public int subscriberCount() {
        return subscribers.size();
    }
}

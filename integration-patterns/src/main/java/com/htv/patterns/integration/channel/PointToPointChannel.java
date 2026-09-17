package com.htv.patterns.integration.channel;

import com.htv.patterns.integration.core.*;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class PointToPointChannel<T> implements MessageChannel<T> {
    private final BlockingQueue<Message<T>> queue = new LinkedBlockingQueue<>();

    public void send(Message<T> message) {
        queue.add(Objects.requireNonNull(message));
    }

    public Message<T> receive() {
        return queue.poll();
    }

    public int size() {
        return queue.size();
    }
}

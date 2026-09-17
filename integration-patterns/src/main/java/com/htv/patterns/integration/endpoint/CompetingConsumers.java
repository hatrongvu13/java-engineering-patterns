package com.htv.patterns.integration.endpoint;

import com.htv.patterns.integration.channel.PointToPointChannel;
import com.htv.patterns.integration.core.*;

import java.util.List;
import java.util.concurrent.*;

public final class CompetingConsumers<T> implements AutoCloseable {
    private final PointToPointChannel<T> channel;
    private final List<MessageHandler<T>> handlers;
    private final ExecutorService pool;
    private volatile boolean running;

    public CompetingConsumers(PointToPointChannel<T> channel, List<MessageHandler<T>> handlers) {
        this.channel = channel;
        this.handlers = List.copyOf(handlers);
        this.pool = Executors.newFixedThreadPool(handlers.size());
    }

    public void start() {
        running = true;
        handlers.forEach(h -> pool.submit(() -> {
            while (running) {
                var m = channel.receive();
                if (m != null) h.handle(m);
                else Thread.onSpinWait();
            }
        }));
    }

    public void close() {
        running = false;
        pool.shutdownNow();
    }
}

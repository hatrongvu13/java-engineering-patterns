package com.htv.patterns.distributed.outbox;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public final class InMemoryOutbox {
    private final Queue<OutboxEvent> events = new ConcurrentLinkedQueue<>();

    public void save(OutboxEvent e) {
        events.add(e);
    }

    public int publishPending(Consumer<OutboxEvent> publisher) {
        int n = 0;
        for (OutboxEvent e; (e = events.peek()) != null; ) {
            publisher.accept(e);
            events.remove(e);
            n++;
        }
        return n;
    }

    public int size() {
        return events.size();
    }
}

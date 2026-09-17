package com.htv.patterns.casestudies.eventprocessing;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class EventProcessingCaseStudy {
    private final Set<String> processed = ConcurrentHashMap.newKeySet();

    public boolean consume(String eventId, String payload, Consumer<String> handler) {
        if (!processed.add(eventId)) return false;
        try {
            handler.accept(payload);
            return true;
        } catch (RuntimeException e) {
            processed.remove(eventId);
            throw e;
        }
    }
}

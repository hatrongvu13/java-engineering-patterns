package com.htv.patterns.integration.routing;

import com.htv.patterns.integration.core.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class Aggregator<T, R> {
    private final Map<String, List<Message<T>>> groups = new ConcurrentHashMap<>();
    private final Function<List<Message<T>>, R> aggregate;

    public Aggregator(Function<List<Message<T>>, R> aggregate) {
        this.aggregate = aggregate;
    }

    public Optional<Message<R>> add(Message<T> part) {
        String id = String.valueOf(part.headers().get("correlationId"));
        int expected = (int) part.headers().get("sequenceSize");
        var list = groups.computeIfAbsent(id, k -> Collections.synchronizedList(new ArrayList<>()));
        list.add(part);
        if (list.size() < expected) return Optional.empty();
        groups.remove(id);
        return Optional.of(Message.of(aggregate.apply(List.copyOf(list))).withHeader("correlationId", id));
    }
}

package com.htv.patterns.integration.routing;

import com.htv.patterns.integration.core.Message;

import java.util.*;
import java.util.function.Function;

public final class Splitter<T, R> {
    private final Function<T, List<R>> split;

    public Splitter(Function<T, List<R>> split) {
        this.split = split;
    }

    public List<Message<R>> apply(Message<T> message) {
        var parts = split.apply(message.payload());
        var result = new ArrayList<Message<R>>();
        for (int i = 0; i < parts.size(); i++)
            result.add(Message.of(parts.get(i))
                    .withHeader("correlationId", message.id()).withHeader("sequenceNumber", i + 1).withHeader("sequenceSize", parts.size()));
        return List.copyOf(result);
    }
}

package com.htv.patterns.integration.transformation;

import com.htv.patterns.integration.core.Message;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class ContentEnricher<T, E, R> {
    private final Function<Message<T>, E> lookup;
    private final BiFunction<T, E, R> merge;

    public ContentEnricher(Function<Message<T>, E> lookup, BiFunction<T, E, R> merge) {
        this.lookup = lookup;
        this.merge = merge;
    }

    public Message<R> enrich(Message<T> message) {
        return message.mapPayload(merge.apply(message.payload(), lookup.apply(message)));
    }
}

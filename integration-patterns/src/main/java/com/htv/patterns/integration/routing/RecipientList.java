package com.htv.patterns.integration.routing;

import com.htv.patterns.integration.channel.MessageChannel;
import com.htv.patterns.integration.core.Message;

import java.util.List;
import java.util.function.Function;

public final class RecipientList<T> {
    private final Function<Message<T>, List<MessageChannel<T>>> resolver;

    public RecipientList(Function<Message<T>, List<MessageChannel<T>>> resolver) {
        this.resolver = resolver;
    }

    public void route(Message<T> message) {
        resolver.apply(message).forEach(c -> c.send(message));
    }
}

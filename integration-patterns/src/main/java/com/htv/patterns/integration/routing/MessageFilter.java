package com.htv.patterns.integration.routing;

import com.htv.patterns.integration.channel.MessageChannel;
import com.htv.patterns.integration.core.Message;

import java.util.function.Predicate;

public final class MessageFilter<T> {
    private final Predicate<Message<T>> predicate;
    private final MessageChannel<T> output;

    public MessageFilter(Predicate<Message<T>> predicate, MessageChannel<T> output) {
        this.predicate = predicate;
        this.output = output;
    }

    public boolean accept(Message<T> message) {
        if (!predicate.test(message)) return false;
        output.send(message);
        return true;
    }
}

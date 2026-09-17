package com.htv.patterns.integration.routing;

import com.htv.patterns.integration.channel.MessageChannel;
import com.htv.patterns.integration.core.Message;

import java.util.*;
import java.util.function.Predicate;

public final class ContentBasedRouter<T> {
    private record Route<T>(Predicate<Message<T>> test, MessageChannel<T> channel) {
    }

    private final List<Route<T>> routes = new ArrayList<>();
    private MessageChannel<T> defaultChannel;

    public ContentBasedRouter<T> when(Predicate<Message<T>> test, MessageChannel<T> channel) {
        routes.add(new Route<>(test, channel));
        return this;
    }

    public ContentBasedRouter<T> otherwise(MessageChannel<T> channel) {
        defaultChannel = channel;
        return this;
    }

    public void route(Message<T> message) {
        routes.stream().filter(r -> r.test.test(message)).findFirst()
                .map(Route::channel).orElseGet(() -> Objects.requireNonNull(defaultChannel, "No matching/default route"))
                .send(message);
    }
}

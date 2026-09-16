package com.htv.patterns.structural.bridge.abstraction;

import com.htv.patterns.structural.bridge.implementor.MessageSender;

import java.util.Objects;

/**
 * Abstraction side of the bridge. Holds a {@link MessageSender}
 * implementor and defines {@link #notify(String)}; concrete
 * refinements decide how the payload is rendered.
 */
public abstract class Notification {

    protected final MessageSender sender;

    protected Notification(
            MessageSender sender
    ) {
        this.sender = Objects.requireNonNull(
                sender,
                "sender must not be null"
        );
    }

    public abstract String notify(
            String recipient
    );

    public String channel() {
        return sender.channel();
    }
}

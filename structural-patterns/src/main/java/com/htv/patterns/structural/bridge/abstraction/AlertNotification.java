package com.htv.patterns.structural.bridge.abstraction;

import com.htv.patterns.structural.bridge.implementor.MessageSender;

/**
 * Refined abstraction: an urgent alert. Works over any
 * {@link MessageSender} without subclass explosion.
 */
public final class AlertNotification extends Notification {

    private final String message;

    public AlertNotification(
            MessageSender sender,
            String message
    ) {
        super(sender);
        this.message = message == null ? "" : message.trim();
    }

    @Override
    public String notify(
            String recipient
    ) {
        return sender.send(
                recipient,
                "[ALERT] " + message
        );
    }
}

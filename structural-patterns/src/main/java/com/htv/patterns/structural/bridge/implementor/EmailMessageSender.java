package com.htv.patterns.structural.bridge.implementor;

/**
 * Concrete implementor: delivers over email.
 */
public final class EmailMessageSender
        implements MessageSender {

    @Override
    public String channel() {
        return "EMAIL";
    }

    @Override
    public String send(
            String recipient,
            String payload
    ) {
        return "email://" + recipient + " | " + payload;
    }
}

package com.htv.patterns.structural.bridge.implementor;

/**
 * Concrete implementor: delivers over SMS.
 */
public final class SmsMessageSender
        implements MessageSender {

    @Override
    public String channel() {
        return "SMS";
    }

    @Override
    public String send(
            String recipient,
            String payload
    ) {
        return "sms://" + recipient + " | " + payload;
    }
}

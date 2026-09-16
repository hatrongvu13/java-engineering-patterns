package com.htv.patterns.structural.bridge.abstraction;

import com.htv.patterns.structural.bridge.implementor.MessageSender;

/**
 * Refined abstraction: a one-time-passcode message.
 */
public final class OtpNotification extends Notification {

    private final String code;

    public OtpNotification(
            MessageSender sender,
            String code
    ) {
        super(sender);

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    "code must not be blank"
            );
        }

        this.code = code.trim();
    }

    @Override
    public String notify(
            String recipient
    ) {
        return sender.send(
                recipient,
                "Your verification code is " + code
        );
    }
}

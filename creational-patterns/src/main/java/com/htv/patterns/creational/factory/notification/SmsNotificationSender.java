package com.htv.patterns.creational.factory.notification;

import java.time.Instant;
import java.util.UUID;

public final class SmsNotificationSender
        implements NotificationSender {

    private static final int MAX_CONTENT_LENGTH =
            160;

    @Override
    public NotificationType supports() {
        return NotificationType.SMS;
    }

    @Override
    public NotificationResult send(
            NotificationRequest request
    ) {
        if (
                request.content().length()
                        > MAX_CONTENT_LENGTH
        ) {
            throw new IllegalArgumentException(
                    "SMS content must not exceed "
                            + MAX_CONTENT_LENGTH
                            + " characters"
            );
        }

        return new NotificationResult(
                supports(),
                request.recipient(),
                "SMS-" + UUID.randomUUID(),
                Instant.now()
        );
    }
}
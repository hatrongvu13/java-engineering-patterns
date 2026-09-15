package com.htv.patterns.creational.factory.notification;

import java.time.Instant;
import java.util.Objects;

public record NotificationResult(
        NotificationType type,
        String recipient,
        String messageId,
        Instant sentAt
) {

    public NotificationResult {
        Objects.requireNonNull(
                type,
                "type must not be null"
        );

        Objects.requireNonNull(
                recipient,
                "recipient must not be null"
        );

        Objects.requireNonNull(
                messageId,
                "messageId must not be null"
        );

        Objects.requireNonNull(
                sentAt,
                "sentAt must not be null"
        );
    }
}

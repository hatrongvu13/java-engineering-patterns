package com.htv.patterns.creational.factory.notification;

import java.time.Instant;
import java.util.UUID;

public final class EmailNotificationSender
        implements NotificationSender {

    @Override
    public NotificationType supports() {
        return NotificationType.EMAIL;
    }

    @Override
    public NotificationResult send(
            NotificationRequest request
    ) {
        return new NotificationResult(
                supports(),
                request.recipient(),
                "EMAIL-" + UUID.randomUUID(),
                Instant.now()
        );
    }
}
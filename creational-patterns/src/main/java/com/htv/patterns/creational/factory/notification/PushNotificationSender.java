package com.htv.patterns.creational.factory.notification;

import java.time.Instant;
import java.util.UUID;

public final class PushNotificationSender
        implements NotificationSender {

    @Override
    public NotificationType supports() {
        return NotificationType.PUSH;
    }

    @Override
    public NotificationResult send(
            NotificationRequest request
    ) {
        return new NotificationResult(
                supports(),
                request.recipient(),
                "PUSH-" + UUID.randomUUID(),
                Instant.now()
        );
    }
}
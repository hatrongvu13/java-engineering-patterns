package com.htv.patterns.creational.factory.registry;

import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationResult;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;

/**
 * Application service that uses an injected Registry-based Factory.
 */
public final class RegistryNotificationService {

    private final NotificationSenderRegistry registry;

    public RegistryNotificationService(
            NotificationSenderRegistry registry
    ) {
        this.registry =
                Objects.requireNonNull(
                        registry,
                        "registry must not be null"
                );
    }

    public NotificationResult send(
            NotificationType type,
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                type,
                "type must not be null"
        );

        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        NotificationSender sender =
                registry.create(type);

        NotificationResult result =
                sender.send(request);

        return Objects.requireNonNull(
                result,
                "NotificationSender.send() must not return null"
        );
    }
}
package com.htv.patterns.creational.factory.method;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.PushNotificationSender;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;

import java.util.Locale;
import java.util.Objects;

public final class NotificationSenderFactory {

    private NotificationSenderFactory() {
        throw new AssertionError(
                "Factory class must not be instantiated"
        );
    }

    public static NotificationSender email() {
        return new EmailNotificationSender();
    }

    public static NotificationSender sms() {
        return new SmsNotificationSender();
    }

    public static NotificationSender push() {
        return new PushNotificationSender();
    }

    public static NotificationSender from(
            NotificationType type
    ) {
        Objects.requireNonNull(
                type,
                "type must not be null"
        );

        return switch (type) {
            case EMAIL -> email();
            case SMS -> sms();
            case PUSH -> push();
        };
    }

    public static NotificationSender from(
            String type
    ) {
        Objects.requireNonNull(
                type,
                "type must not be null"
        );

        String normalized =
                type.trim()
                        .toUpperCase(Locale.ROOT);

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(
                    "type must not be blank"
            );
        }

        try {
            return from(
                    NotificationType.valueOf(
                            normalized
                    )
            );
        } catch (
                IllegalArgumentException exception
        ) {
            throw new IllegalArgumentException(
                    "Unsupported notification type: "
                            + type,
                    exception
            );
        }
    }
}
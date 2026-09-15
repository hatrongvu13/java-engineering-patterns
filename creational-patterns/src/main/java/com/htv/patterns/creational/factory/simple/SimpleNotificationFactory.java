package com.htv.patterns.creational.factory.simple;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.PushNotificationSender;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;

import java.util.Objects;

public final class SimpleNotificationFactory {

    public NotificationSender create(
            NotificationType type
    ) {
        Objects.requireNonNull(
                type,
                "type must not be null"
        );

        return switch (type) {
            case EMAIL ->
                    new EmailNotificationSender();

            case SMS ->
                    new SmsNotificationSender();

            case PUSH ->
                    new PushNotificationSender();
        };
    }
}
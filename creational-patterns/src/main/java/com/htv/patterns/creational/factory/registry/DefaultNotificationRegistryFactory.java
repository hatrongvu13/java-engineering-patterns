package com.htv.patterns.creational.factory.registry;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.PushNotificationSender;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;

/**
 * Provides the default Registry-based Factory configuration.
 */
public final class DefaultNotificationRegistryFactory {

    private DefaultNotificationRegistryFactory() {
        throw new AssertionError(
                "Factory class must not be instantiated"
        );
    }

    public static NotificationSenderRegistry create() {
        return new NotificationSenderRegistryBuilder()
                .register(
                        NotificationType.EMAIL,
                        EmailNotificationSender::new
                )
                .register(
                        NotificationType.SMS,
                        SmsNotificationSender::new
                )
                .register(
                        NotificationType.PUSH,
                        PushNotificationSender::new
                )
                .build();
    }
}
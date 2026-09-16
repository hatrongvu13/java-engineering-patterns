package com.htv.patterns.creational.factory.method;

import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.PushNotificationSender;

/**
 * Creates push notification senders.
 */
public final class PushNotificationCreator
        extends NotificationCreator {

    @Override
    protected NotificationSender createSender() {
        return new PushNotificationSender();
    }
}

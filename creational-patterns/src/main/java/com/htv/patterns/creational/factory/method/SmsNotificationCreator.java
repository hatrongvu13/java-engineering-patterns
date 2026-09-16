package com.htv.patterns.creational.factory.method;

import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;

/**
 * Creates SMS notification senders.
 */
public final class SmsNotificationCreator
        extends NotificationCreator {

    @Override
    protected NotificationSender createSender() {
        return new SmsNotificationSender();
    }
}
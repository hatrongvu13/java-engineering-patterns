package com.htv.patterns.creational.factory.method;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationSender;

/**
 * Creates email notification senders.
 */
public final class EmailNotificationCreator
        extends NotificationCreator {

    @Override
    protected NotificationSender createSender() {
        return new EmailNotificationSender();
    }
}
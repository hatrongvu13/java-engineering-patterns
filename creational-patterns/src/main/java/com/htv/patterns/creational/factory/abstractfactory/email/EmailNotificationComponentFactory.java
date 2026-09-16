package com.htv.patterns.creational.factory.abstractfactory.email;

import com.htv.patterns.creational.factory.abstractfactory.core.NotificationComponentFactory;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationFormatter;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationValidator;
import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;

public final class EmailNotificationComponentFactory
        implements NotificationComponentFactory {

    @Override
    public NotificationType supports() {
        return NotificationType.EMAIL;
    }

    @Override
    public NotificationValidator createValidator() {
        return new EmailAddressValidator();
    }

    @Override
    public NotificationFormatter createFormatter() {
        return new HtmlEmailFormatter();
    }

    @Override
    public NotificationSender createSender() {
        return new EmailNotificationSender();
    }
}
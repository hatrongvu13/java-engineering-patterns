package com.htv.patterns.creational.factory.abstractfactory.push;

import com.htv.patterns.creational.factory.abstractfactory.core.NotificationComponentFactory;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationFormatter;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationValidator;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.PushNotificationSender;

public final class PushNotificationComponentFactory
        implements NotificationComponentFactory {

    @Override
    public NotificationType supports() {
        return NotificationType.PUSH;
    }

    @Override
    public NotificationValidator createValidator() {
        return new DeviceTokenValidator();
    }

    @Override
    public NotificationFormatter createFormatter() {
        return new PushMessageFormatter();
    }

    @Override
    public NotificationSender createSender() {
        return new PushNotificationSender();
    }
}
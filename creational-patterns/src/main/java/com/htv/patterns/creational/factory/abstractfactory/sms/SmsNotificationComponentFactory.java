package com.htv.patterns.creational.factory.abstractfactory.sms;

import com.htv.patterns.creational.factory.abstractfactory.core.NotificationComponentFactory;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationFormatter;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationValidator;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;

public final class SmsNotificationComponentFactory
        implements NotificationComponentFactory {

    @Override
    public NotificationType supports() {
        return NotificationType.SMS;
    }

    @Override
    public NotificationValidator createValidator() {
        return new PhoneNumberValidator();
    }

    @Override
    public NotificationFormatter createFormatter() {
        return new PlainTextSmsFormatter();
    }

    @Override
    public NotificationSender createSender() {
        return new SmsNotificationSender();
    }
}
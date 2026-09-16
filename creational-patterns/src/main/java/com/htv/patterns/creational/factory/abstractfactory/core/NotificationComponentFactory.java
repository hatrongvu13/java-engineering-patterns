package com.htv.patterns.creational.factory.abstractfactory.core;

import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;

public interface NotificationComponentFactory {

    NotificationType supports();

    NotificationValidator createValidator();

    NotificationFormatter createFormatter();

    NotificationSender createSender();
}
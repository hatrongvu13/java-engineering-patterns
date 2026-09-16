package com.htv.patterns.creational.factory.abstractfactory.core;

import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationType;

public interface NotificationFormatter {

    NotificationType supports();

    FormattedNotification format(
            NotificationRequest request
    );
}
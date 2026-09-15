package com.htv.patterns.creational.factory.simple;

import com.htv.patterns.creational.factory.notification.NotificationType;

public final class UnsupportedNotificationTypeException
        extends RuntimeException {

    public UnsupportedNotificationTypeException(
            NotificationType type
    ) {
        super(
                "Unsupported notification type: "
                        + type
        );
    }
}
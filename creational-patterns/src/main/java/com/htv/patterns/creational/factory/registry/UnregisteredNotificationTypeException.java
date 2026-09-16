package com.htv.patterns.creational.factory.registry;

import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;

public final class UnregisteredNotificationTypeException
        extends RuntimeException {

    public UnregisteredNotificationTypeException(
            NotificationType type
    ) {
        super(
                "No notification sender factory is registered for type: "
                        + Objects.requireNonNull(
                        type,
                        "type must not be null"
                )
        );
    }
}

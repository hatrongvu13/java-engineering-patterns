package com.htv.patterns.creational.factory.registry;

import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;

public final class DuplicateNotificationTypeException
        extends RuntimeException {

    public DuplicateNotificationTypeException(
            NotificationType type
    ) {
        super(
                "A notification sender factory is already registered for type: "
                        + Objects.requireNonNull(
                        type,
                        "type must not be null"
                )
        );
    }
}

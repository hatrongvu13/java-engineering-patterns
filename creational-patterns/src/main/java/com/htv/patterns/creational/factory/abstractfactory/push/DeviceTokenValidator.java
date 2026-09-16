package com.htv.patterns.creational.factory.abstractfactory.push;

import com.htv.patterns.creational.factory.abstractfactory.core.NotificationValidator;
import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;

public final class DeviceTokenValidator
        implements NotificationValidator {

    private static final int MINIMUM_TOKEN_LENGTH =
            20;

    @Override
    public NotificationType supports() {
        return NotificationType.PUSH;
    }

    @Override
    public void validate(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        if (
                request.recipient().length()
                        < MINIMUM_TOKEN_LENGTH
        ) {
            throw new IllegalArgumentException(
                    "Device token must contain at least "
                            + MINIMUM_TOKEN_LENGTH
                            + " characters"
            );
        }
    }
}
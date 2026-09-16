package com.htv.patterns.creational.factory.abstractfactory.email;

import com.htv.patterns.creational.factory.abstractfactory.core.NotificationValidator;
import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;
import java.util.regex.Pattern;

public final class EmailAddressValidator
        implements NotificationValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
            );

    @Override
    public NotificationType supports() {
        return NotificationType.EMAIL;
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
                !EMAIL_PATTERN
                        .matcher(request.recipient())
                        .matches()
        ) {
            throw new IllegalArgumentException(
                    "Invalid email address: "
                            + request.recipient()
            );
        }

        if (request.subject().isBlank()) {
            throw new IllegalArgumentException(
                    "Email subject must not be blank"
            );
        }
    }
}
package com.htv.patterns.creational.factory.abstractfactory.sms;

import com.htv.patterns.creational.factory.abstractfactory.core.NotificationValidator;
import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;
import java.util.regex.Pattern;

public final class PhoneNumberValidator
        implements NotificationValidator {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[1-9]\\d{7,14}$");

    private static final int MAX_CONTENT_LENGTH =
            160;

    @Override
    public NotificationType supports() {
        return NotificationType.SMS;
    }

    @Override
    public void validate(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        String normalizedPhone =
                normalizePhone(request.recipient());

        if (
                !PHONE_PATTERN
                        .matcher(normalizedPhone)
                        .matches()
        ) {
            throw new IllegalArgumentException(
                    "Invalid phone number: "
                            + request.recipient()
            );
        }

        if (
                request.content().length()
                        > MAX_CONTENT_LENGTH
        ) {
            throw new IllegalArgumentException(
                    "SMS content must not exceed "
                            + MAX_CONTENT_LENGTH
                            + " characters"
            );
        }
    }

    private static String normalizePhone(
            String phone
    ) {
        return phone.replaceAll(
                "[\\s().-]",
                ""
        );
    }
}
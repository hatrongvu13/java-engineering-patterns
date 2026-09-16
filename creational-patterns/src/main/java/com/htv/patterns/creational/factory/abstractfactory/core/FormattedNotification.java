package com.htv.patterns.creational.factory.abstractfactory.core;

import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;

public record FormattedNotification(
        NotificationType type,
        String recipient,
        String subject,
        String body,
        String contentType
) {

    public FormattedNotification {
        Objects.requireNonNull(
                type,
                "type must not be null"
        );

        recipient = requireText(
                recipient,
                "recipient"
        );

        subject = subject == null
                ? ""
                : subject.trim();

        body = requireText(
                body,
                "body"
        );

        contentType = requireText(
                contentType,
                "contentType"
        );
    }

    private static String requireText(
            String value,
            String fieldName
    ) {
        Objects.requireNonNull(
                value,
                fieldName + " must not be null"
        );

        String normalized = value.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(
                    fieldName + " must not be blank"
            );
        }

        return normalized;
    }
}
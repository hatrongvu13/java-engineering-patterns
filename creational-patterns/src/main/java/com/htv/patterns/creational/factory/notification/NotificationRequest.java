package com.htv.patterns.creational.factory.notification;

import java.util.Objects;

public record NotificationRequest(
        String recipient,
        String subject,
        String content
) {

    public NotificationRequest {
        recipient = requireText(
                recipient,
                "recipient"
        );

        content = requireText(
                content,
                "content"
        );

        subject = subject == null
                ? ""
                : subject.trim();
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
package com.htv.patterns.behavioral.strategy.core;

import java.util.Map;
import java.util.Objects;

public record NotificationMessage(
        NotificationChannel channel,
        String recipient,
        String subject,
        String body,
        String contentType,
        Map<String, String> metadata
) {

    public NotificationMessage {
        Objects.requireNonNull(
                channel,
                "channel must not be null"
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

        metadata = metadata == null
                ? Map.of()
                : Map.copyOf(metadata);
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
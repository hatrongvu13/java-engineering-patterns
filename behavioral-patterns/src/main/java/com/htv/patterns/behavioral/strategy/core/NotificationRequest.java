package com.htv.patterns.behavioral.strategy.core;

import java.util.Map;
import java.util.Objects;

public record NotificationRequest(
        String recipient,
        String subject,
        String content,
        Map<String, String> attributes
) {

    public NotificationRequest {
        recipient = requireText(
                recipient,
                "recipient"
        );

        subject = subject == null
                ? ""
                : subject.trim();

        content = requireText(
                content,
                "content"
        );

        attributes = attributes == null
                ? Map.of()
                : Map.copyOf(attributes);
    }

    public NotificationRequest(
            String recipient,
            String subject,
            String content
    ) {
        this(
                recipient,
                subject,
                content,
                Map.of()
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
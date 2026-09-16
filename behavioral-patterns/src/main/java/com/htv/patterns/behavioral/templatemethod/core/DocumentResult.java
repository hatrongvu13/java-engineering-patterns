package com.htv.patterns.behavioral.templatemethod.core;

import java.time.Instant;
import java.util.Objects;

public record DocumentResult(
        DocumentFormat format,
        String fileName,
        String contentType,
        String location,
        int contentLength,
        Instant completedAt
) {

    public DocumentResult {
        Objects.requireNonNull(
                format,
                "format must not be null"
        );

        fileName = requireText(
                fileName,
                "fileName"
        );

        contentType = requireText(
                contentType,
                "contentType"
        );

        location = requireText(
                location,
                "location"
        );

        if (contentLength <= 0) {
            throw new IllegalArgumentException(
                    "contentLength must be positive"
            );
        }

        Objects.requireNonNull(
                completedAt,
                "completedAt must not be null"
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
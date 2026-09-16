package com.htv.patterns.behavioral.observer.event;

import com.htv.patterns.behavioral.observer.core.DomainEvent;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record OcrProcessingFailed(
        UUID eventId,
        Instant occurredAt,
        String aggregateId,
        String errorCode,
        String errorMessage
) implements DomainEvent {

    public OcrProcessingFailed {
        Objects.requireNonNull(
                eventId,
                "eventId must not be null"
        );

        Objects.requireNonNull(
                occurredAt,
                "occurredAt must not be null"
        );

        aggregateId = requireText(
                aggregateId,
                "aggregateId"
        );

        errorCode = requireText(
                errorCode,
                "errorCode"
        );

        errorMessage = requireText(
                errorMessage,
                "errorMessage"
        );
    }

    public static OcrProcessingFailed create(
            String documentId,
            String errorCode,
            String errorMessage,
            Instant occurredAt
    ) {
        return new OcrProcessingFailed(
                UUID.randomUUID(),
                occurredAt,
                documentId,
                errorCode,
                errorMessage
        );
    }

    @Override
    public String eventType() {
        return "ocr.processing.failed";
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
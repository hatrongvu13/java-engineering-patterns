package com.htv.patterns.behavioral.observer.event;

import com.htv.patterns.behavioral.observer.core.DomainEvent;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record OcrProcessingStarted(
        UUID eventId,
        Instant occurredAt,
        String aggregateId,
        String fileName
) implements DomainEvent {

    public OcrProcessingStarted {
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

        fileName = requireText(
                fileName,
                "fileName"
        );
    }

    public static OcrProcessingStarted create(
            String documentId,
            String fileName,
            Instant occurredAt
    ) {
        return new OcrProcessingStarted(
                UUID.randomUUID(),
                occurredAt,
                documentId,
                fileName
        );
    }

    @Override
    public String eventType() {
        return "ocr.processing.started";
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
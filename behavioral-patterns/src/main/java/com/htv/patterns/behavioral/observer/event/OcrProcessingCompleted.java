package com.htv.patterns.behavioral.observer.event;

import com.htv.patterns.behavioral.observer.core.DomainEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record OcrProcessingCompleted(
        UUID eventId,
        Instant occurredAt,
        String aggregateId,
        String recognizedText,
        double confidence,
        Duration processingDuration
) implements DomainEvent {

    public OcrProcessingCompleted {
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

        recognizedText = requireText(
                recognizedText,
                "recognizedText"
        );

        if (
                Double.isNaN(confidence)
                        || confidence < 0.0D
                        || confidence > 1.0D
        ) {
            throw new IllegalArgumentException(
                    "confidence must be between 0.0 and 1.0"
            );
        }

        Objects.requireNonNull(
                processingDuration,
                "processingDuration must not be null"
        );

        if (processingDuration.isNegative()) {
            throw new IllegalArgumentException(
                    "processingDuration must not be negative"
            );
        }
    }

    public static OcrProcessingCompleted create(
            String documentId,
            String recognizedText,
            double confidence,
            Duration processingDuration,
            Instant occurredAt
    ) {
        return new OcrProcessingCompleted(
                UUID.randomUUID(),
                occurredAt,
                documentId,
                recognizedText,
                confidence,
                processingDuration
        );
    }

    @Override
    public String eventType() {
        return "ocr.processing.completed";
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
package com.htv.patterns.behavioral.observer.core;

import java.util.List;
import java.util.Objects;

public record EventPublicationResult(
        String eventType,
        int listenerCount,
        int successCount,
        List<ListenerFailure> failures
) {

    public EventPublicationResult {
        eventType = requireText(
                eventType,
                "eventType"
        );

        if (listenerCount < 0) {
            throw new IllegalArgumentException(
                    "listenerCount must not be negative"
            );
        }

        if (successCount < 0) {
            throw new IllegalArgumentException(
                    "successCount must not be negative"
            );
        }

        failures = failures == null
                ? List.of()
                : List.copyOf(failures);

        if (
                successCount + failures.size()
                        != listenerCount
        ) {
            throw new IllegalArgumentException(
                    "Listener result count is inconsistent"
            );
        }
    }

    public boolean successful() {
        return failures.isEmpty();
    }

    public int failureCount() {
        return failures.size();
    }

    public record ListenerFailure(
            String listenerName,
            String message,
            Throwable cause
    ) {

        public ListenerFailure {
            listenerName = requireText(
                    listenerName,
                    "listenerName"
            );

            message = requireText(
                    message,
                    "message"
            );

            Objects.requireNonNull(
                    cause,
                    "cause must not be null"
            );
        }
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
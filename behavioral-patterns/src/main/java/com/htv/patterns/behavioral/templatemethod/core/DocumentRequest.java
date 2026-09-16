package com.htv.patterns.behavioral.templatemethod.core;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public record DocumentRequest(
        String templateCode,
        String outputName,
        String recipient,
        String language,
        Map<String, Object> parameters
) {

    public DocumentRequest {
        templateCode = requireText(
                templateCode,
                "templateCode"
        );

        outputName = requireText(
                outputName,
                "outputName"
        );

        recipient = recipient == null
                ? ""
                : recipient.trim();

        language = normalizeLanguage(language);

        parameters = parameters == null
                ? Map.of()
                : Map.copyOf(
                new LinkedHashMap<>(parameters)
        );
    }

    public DocumentRequest(
            String templateCode,
            String outputName,
            Map<String, Object> parameters
    ) {
        this(
                templateCode,
                outputName,
                "",
                "vi",
                parameters
        );
    }

    private static String normalizeLanguage(
            String value
    ) {
        if (value == null || value.isBlank()) {
            return "vi";
        }

        String normalized = value
                .trim()
                .toLowerCase(Locale.ROOT);

        if (
                !normalized.equals("vi")
                        && !normalized.equals("en")
        ) {
            throw new IllegalArgumentException(
                    "Unsupported language: " + value
            );
        }

        return normalized;
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
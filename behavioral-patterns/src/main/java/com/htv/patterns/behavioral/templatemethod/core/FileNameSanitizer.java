package com.htv.patterns.behavioral.templatemethod.core;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;

public final class FileNameSanitizer {

    private static final int MAXIMUM_LENGTH = 120;

    private FileNameSanitizer() {
        throw new AssertionError(
                "Utility class must not be instantiated"
        );
    }

    public static String sanitize(
            String value
    ) {
        Objects.requireNonNull(
                value,
                "fileName must not be null"
        );

        String normalized = Normalizer.normalize(
                value.trim(),
                Normalizer.Form.NFD
        );

        normalized = normalized
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9._-]+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^[._-]+", "")
                .replaceAll("[._-]+$", "");

        if (normalized.isBlank()) {
            throw new IllegalArgumentException(
                    "fileName must contain at least one valid character"
            );
        }

        if (normalized.length() > MAXIMUM_LENGTH) {
            normalized = normalized.substring(
                    0,
                    MAXIMUM_LENGTH
            );
        }

        return normalized;
    }

    public static String withExtension(
            String baseName,
            DocumentFormat format
    ) {
        Objects.requireNonNull(
                format,
                "format must not be null"
        );

        String sanitized = sanitize(baseName);
        String suffix = "." + format.extension();

        if (sanitized.endsWith(suffix)) {
            return sanitized;
        }

        return sanitized + suffix;
    }
}
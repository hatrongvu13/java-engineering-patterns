package com.htv.patterns.behavioral.templatemethod.core;

import java.util.Arrays;
import java.util.Objects;

public final class RenderedDocument {

    private final DocumentFormat format;
    private final String fileName;
    private final byte[] content;

    public RenderedDocument(
            DocumentFormat format,
            String fileName,
            byte[] content
    ) {
        this.format = Objects.requireNonNull(
                format,
                "format must not be null"
        );

        this.fileName = requireText(
                fileName,
                "fileName"
        );

        Objects.requireNonNull(
                content,
                "content must not be null"
        );

        if (content.length == 0) {
            throw new IllegalArgumentException(
                    "content must not be empty"
            );
        }

        this.content = Arrays.copyOf(
                content,
                content.length
        );
    }

    public DocumentFormat format() {
        return format;
    }

    public String fileName() {
        return fileName;
    }

    public String contentType() {
        return format.contentType();
    }

    public int contentLength() {
        return content.length;
    }

    public byte[] content() {
        return Arrays.copyOf(
                content,
                content.length
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
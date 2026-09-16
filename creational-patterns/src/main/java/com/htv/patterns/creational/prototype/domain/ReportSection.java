package com.htv.patterns.creational.prototype.domain;

import java.util.Objects;

public final class ReportSection {

    private String code;
    private String title;
    private boolean enabled;

    public ReportSection(
            String code,
            String title,
            boolean enabled
    ) {
        this.code = requireText(
                code,
                "code"
        );

        this.title = requireText(
                title,
                "title"
        );

        this.enabled = enabled;
    }

    public ReportSection(
            ReportSection source
    ) {
        Objects.requireNonNull(
                source,
                "source must not be null"
        );

        this.code = source.code;
        this.title = source.title;
        this.enabled = source.enabled;
    }

    public String getCode() {
        return code;
    }

    public void setCode(
            String code
    ) {
        this.code = requireText(
                code,
                "code"
        );
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(
            String title
    ) {
        this.title = requireText(
                title,
                "title"
        );
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(
            boolean enabled
    ) {
        this.enabled = enabled;
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

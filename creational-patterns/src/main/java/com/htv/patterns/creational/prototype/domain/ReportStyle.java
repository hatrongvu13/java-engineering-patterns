package com.htv.patterns.creational.prototype.domain;

import java.util.Objects;

public final class ReportStyle {

    private String fontName;
    private String primaryColor;
    private int fontSize;

    public ReportStyle(
            String fontName,
            String primaryColor,
            int fontSize
    ) {
        this.fontName = requireText(
                fontName,
                "fontName"
        );

        this.primaryColor = requireText(
                primaryColor,
                "primaryColor"
        );

        setFontSize(fontSize);
    }

    public ReportStyle(
            ReportStyle source
    ) {
        Objects.requireNonNull(
                source,
                "source must not be null"
        );

        this.fontName = source.fontName;
        this.primaryColor =
                source.primaryColor;

        this.fontSize = source.fontSize;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(
            String fontName
    ) {
        this.fontName = requireText(
                fontName,
                "fontName"
        );
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(
            String primaryColor
    ) {
        this.primaryColor = requireText(
                primaryColor,
                "primaryColor"
        );
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(
            int fontSize
    ) {
        if (fontSize <= 0) {
            throw new IllegalArgumentException(
                    "fontSize must be positive"
            );
        }

        this.fontSize = fontSize;
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
package com.htv.patterns.behavioral.templatemethod.core;

public enum DocumentFormat {

    HTML("html", "text/html"),
    EXCEL("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    WORD("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private final String extension;
    private final String contentType;

    DocumentFormat(
            String extension,
            String contentType
    ) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String extension() {
        return extension;
    }

    public String contentType() {
        return contentType;
    }
}

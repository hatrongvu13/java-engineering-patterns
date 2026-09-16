package com.htv.patterns.behavioral.templatemethod.core;

public final class DocumentGenerationException
        extends RuntimeException {

    public DocumentGenerationException(
            String message
    ) {
        super(message);
    }

    public DocumentGenerationException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}
package com.htv.patterns.behavioral.chain.core;

public final class OcrProcessingException
        extends RuntimeException {

    private final String handlerCode;

    public OcrProcessingException(
            String handlerCode,
            String message
    ) {
        super(message);
        this.handlerCode = handlerCode;
    }

    public OcrProcessingException(
            String handlerCode,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.handlerCode = handlerCode;
    }

    public String handlerCode() {
        return handlerCode;
    }
}
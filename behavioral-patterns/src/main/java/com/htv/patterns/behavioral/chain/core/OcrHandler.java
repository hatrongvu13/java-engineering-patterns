package com.htv.patterns.behavioral.chain.core;

public interface OcrHandler {

    String code();

    OcrHandler setNext(
            OcrHandler next
    );

    OcrProcessingContext handle(
            OcrProcessingContext context
    );
}
package com.htv.patterns.behavioral.chain.pipeline;

import com.htv.patterns.behavioral.chain.core.OcrHandler;
import com.htv.patterns.behavioral.chain.core.OcrProcessingContext;

import java.util.Objects;

public final class OcrProcessingChain {

    private final OcrHandler firstHandler;

    public OcrProcessingChain(
            OcrHandler firstHandler
    ) {
        this.firstHandler =
                Objects.requireNonNull(
                        firstHandler,
                        "firstHandler must not be null"
                );
    }

    public OcrProcessingContext process(
            OcrProcessingContext context
    ) {
        Objects.requireNonNull(
                context,
                "context must not be null"
        );

        return firstHandler.handle(context);
    }
}
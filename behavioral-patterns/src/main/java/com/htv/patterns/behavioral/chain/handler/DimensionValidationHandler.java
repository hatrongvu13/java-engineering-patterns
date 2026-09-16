package com.htv.patterns.behavioral.chain.handler;

import com.htv.patterns.behavioral.chain.core.AbstractOcrHandler;
import com.htv.patterns.behavioral.chain.core.OcrDocumentType;
import com.htv.patterns.behavioral.chain.core.OcrProcessingContext;
import com.htv.patterns.behavioral.chain.core.OcrProcessingException;

public final class DimensionValidationHandler
        extends AbstractOcrHandler {

    public static final String CODE =
            "dimension-validation";

    private static final int MINIMUM_WIDTH =
            600;

    private static final int MINIMUM_HEIGHT =
            600;

    @Override
    public String code() {
        return CODE;
    }

    @Override
    protected OcrProcessingContext process(
            OcrProcessingContext context
    ) {
        if (
                context.documentType()
                        != OcrDocumentType.IMAGE
        ) {
            return context;
        }

        if (
                context.width() < MINIMUM_WIDTH
                        || context.height()
                        < MINIMUM_HEIGHT
        ) {
            throw new OcrProcessingException(
                    code(),
                    "Image dimensions must be at least "
                            + MINIMUM_WIDTH
                            + " x "
                            + MINIMUM_HEIGHT
            );
        }

        return context;
    }
}
package com.htv.patterns.behavioral.chain.handler;

import com.htv.patterns.behavioral.chain.core.AbstractOcrHandler;
import com.htv.patterns.behavioral.chain.core.OcrDocumentType;
import com.htv.patterns.behavioral.chain.core.OcrProcessingContext;
import com.htv.patterns.behavioral.chain.core.OcrProcessingException;

public final class ImageQualityHandler
        extends AbstractOcrHandler {

    public static final String CODE =
            "image-quality";

    private static final double
            MINIMUM_QUALITY_SCORE = 0.65D;

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
                Double.isNaN(
                        context.qualityScore()
                )
                        || context.qualityScore() < 0.0D
                        || context.qualityScore() > 1.0D
        ) {
            throw new OcrProcessingException(
                    code(),
                    "Image quality score must be "
                            + "between 0.0 and 1.0"
            );
        }

        if (
                context.qualityScore()
                        < MINIMUM_QUALITY_SCORE
        ) {
            throw new OcrProcessingException(
                    code(),
                    "Image quality score must be at least "
                            + MINIMUM_QUALITY_SCORE
            );
        }

        return context;
    }
}
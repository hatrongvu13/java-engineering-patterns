package com.htv.patterns.behavioral.chain.handler;

import com.htv.patterns.behavioral.chain.core.AbstractOcrHandler;
import com.htv.patterns.behavioral.chain.core.OcrDocumentType;
import com.htv.patterns.behavioral.chain.core.OcrProcessingContext;
import com.htv.patterns.behavioral.chain.core.OcrProcessingStatus;

public final class NoiseReductionHandler
        extends AbstractOcrHandler {

    public static final String CODE =
            "noise-reduction";

    private static final double
            TARGET_QUALITY_SCORE = 0.85D;

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

        context.setStatus(
                OcrProcessingStatus.PREPROCESSING
        );

        double originalScore =
                context.qualityScore();

        context.putAttribute(
                "qualityBeforeNoiseReduction",
                originalScore
        );

        double improvedScore =
                Math.max(
                        originalScore,
                        TARGET_QUALITY_SCORE
                );

        context.setQualityScore(
                improvedScore
        );

        context.markNoiseReduced();

        context.putAttribute(
                "qualityAfterNoiseReduction",
                improvedScore
        );

        return context;
    }
}
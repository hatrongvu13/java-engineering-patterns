package com.htv.patterns.behavioral.chain.handler;

import com.htv.patterns.behavioral.chain.core.AbstractOcrHandler;
import com.htv.patterns.behavioral.chain.core.OcrDocumentType;
import com.htv.patterns.behavioral.chain.core.OcrProcessingContext;
import com.htv.patterns.behavioral.chain.core.OcrProcessingException;

public final class OcrReadinessHandler
        extends AbstractOcrHandler {

    public static final String CODE =
            "ocr-readiness";

    private static final double
            MINIMUM_READY_SCORE = 0.75D;

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
                        == OcrDocumentType.UNKNOWN
        ) {
            throw new OcrProcessingException(
                    code(),
                    "Unknown document type "
                            + "cannot be processed"
            );
        }

        if (
                context.documentType()
                        == OcrDocumentType.IMAGE
                        && context.qualityScore()
                        < MINIMUM_READY_SCORE
        ) {
            throw new OcrProcessingException(
                    code(),
                    "Document is not ready for OCR"
            );
        }

        context.putAttribute(
                "ocrReady",
                true
        );

        context.markReadyForOcr();

        return context;
    }
}
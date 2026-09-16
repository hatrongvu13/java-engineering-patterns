package com.htv.patterns.behavioral.chain.handler;

import com.htv.patterns.behavioral.chain.core.AbstractOcrHandler;
import com.htv.patterns.behavioral.chain.core.OcrDocumentType;
import com.htv.patterns.behavioral.chain.core.OcrProcessingContext;
import com.htv.patterns.behavioral.chain.core.OcrProcessingException;
import com.htv.patterns.behavioral.chain.core.OcrProcessingStatus;

import java.util.Set;

public final class FileValidationHandler
        extends AbstractOcrHandler {

    public static final String CODE =
            "file-validation";

    private static final int MAXIMUM_SIZE_BYTES =
            10 * 1024 * 1024;

    private static final Set<String>
            SUPPORTED_IMAGE_EXTENSIONS =
            Set.of(
                    "jpg",
                    "jpeg",
                    "png",
                    "webp"
            );

    private static final Set<String>
            SUPPORTED_PDF_EXTENSIONS =
            Set.of("pdf");

    @Override
    public String code() {
        return CODE;
    }

    @Override
    protected OcrProcessingContext process(
            OcrProcessingContext context
    ) {
        context.setStatus(
                OcrProcessingStatus.VALIDATING
        );

        if (context.contentLength() == 0) {
            throw new OcrProcessingException(
                    code(),
                    "Document content must not be empty"
            );
        }

        if (
                context.contentLength()
                        > MAXIMUM_SIZE_BYTES
        ) {
            throw new OcrProcessingException(
                    code(),
                    "Document size must not exceed "
                            + MAXIMUM_SIZE_BYTES
                            + " bytes"
            );
        }

        validateExtension(context);

        return context;
    }

    private void validateExtension(
            OcrProcessingContext context
    ) {
        String extension =
                context.extension();

        boolean supported =
                switch (context.documentType()) {
                    case IMAGE ->
                            SUPPORTED_IMAGE_EXTENSIONS
                                    .contains(extension);

                    case PDF ->
                            SUPPORTED_PDF_EXTENSIONS
                                    .contains(extension);

                    case UNKNOWN ->
                            false;
                };

        if (!supported) {
            throw new OcrProcessingException(
                    code(),
                    "Unsupported document extension: "
                            + extension
            );
        }
    }
}
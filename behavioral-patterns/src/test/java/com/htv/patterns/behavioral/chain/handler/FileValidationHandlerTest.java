package com.htv.patterns.behavioral.chain.handler;

import com.htv.patterns.behavioral.chain.core.OcrDocumentType;
import com.htv.patterns.behavioral.chain.core.OcrProcessingContext;
import com.htv.patterns.behavioral.chain.core.OcrProcessingException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileValidationHandlerTest {

    private final FileValidationHandler handler =
            new FileValidationHandler();

    @Test
    void shouldAcceptSupportedImage() {
        OcrProcessingContext context =
                imageContext("exam.png");

        OcrProcessingContext result =
                handler.handle(context);

        assertThat(result)
                .isSameAs(context);

        assertThat(
                result.processingHistory()
        ).containsExactly(
                FileValidationHandler.CODE
        );
    }

    @Test
    void shouldRejectUnsupportedExtension() {
        OcrProcessingContext context =
                imageContext("exam.exe");

        assertThatThrownBy(
                () -> handler.handle(context)
        )
                .isInstanceOf(
                        OcrProcessingException.class
                )
                .hasMessageContaining(
                        "Unsupported document extension"
                );
    }

    @Test
    void shouldAcceptPdfDocument() {
        OcrProcessingContext context =
                new OcrProcessingContext(
                        "DOC-002",
                        "exam.pdf",
                        OcrDocumentType.PDF,
                        "pdf-content".getBytes(
                                StandardCharsets.UTF_8
                        ),
                        0,
                        0,
                        0.0D,
                        0
                );

        assertThat(
                handler.handle(context)
        ).isSameAs(context);
    }

    private static OcrProcessingContext
    imageContext(
            String fileName
    ) {
        return new OcrProcessingContext(
                "DOC-001",
                fileName,
                OcrDocumentType.IMAGE,
                "image-content".getBytes(
                        StandardCharsets.UTF_8
                ),
                1200,
                1600,
                0.80D,
                0
        );
    }
}
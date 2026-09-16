package com.htv.patterns.behavioral.chain.pipeline;

import com.htv.patterns.behavioral.chain.core.OcrDocumentType;
import com.htv.patterns.behavioral.chain.core.OcrProcessingContext;
import com.htv.patterns.behavioral.chain.core.OcrProcessingException;
import com.htv.patterns.behavioral.chain.core.OcrProcessingStatus;
import com.htv.patterns.behavioral.chain.handler.DimensionValidationHandler;
import com.htv.patterns.behavioral.chain.handler.FileValidationHandler;
import com.htv.patterns.behavioral.chain.handler.ImageQualityHandler;
import com.htv.patterns.behavioral.chain.handler.NoiseReductionHandler;
import com.htv.patterns.behavioral.chain.handler.OcrReadinessHandler;
import com.htv.patterns.behavioral.chain.handler.OrientationHandler;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultOcrProcessingChainTest {

    @Test
    void shouldProcessValidImageThroughEntireChain() {
        OcrProcessingChain chain =
                DefaultOcrProcessingChain.create();

        OcrProcessingContext context =
                new OcrProcessingContext(
                        "DOC-001",
                        "exam-paper.png",
                        OcrDocumentType.IMAGE,
                        "image-content".getBytes(
                                StandardCharsets.UTF_8
                        ),
                        1600,
                        2400,
                        0.78D,
                        88
                );

        OcrProcessingContext result =
                chain.process(context);

        assertThat(result)
                .isSameAs(context);

        assertThat(result.status())
                .isEqualTo(
                        OcrProcessingStatus.READY
                );

        assertThat(result.readyForOcr())
                .isTrue();

        assertThat(result.noiseReduced())
                .isTrue();

        assertThat(
                result.orientationDegrees()
        ).isEqualTo(90);

        assertThat(
                result.qualityScore()
        ).isEqualTo(0.85D);

        assertThat(
                result.processingHistory()
        ).containsExactly(
                FileValidationHandler.CODE,
                DimensionValidationHandler.CODE,
                ImageQualityHandler.CODE,
                OrientationHandler.CODE,
                NoiseReductionHandler.CODE,
                OcrReadinessHandler.CODE
        );
    }

    @Test
    void shouldStopAtDimensionValidation() {
        OcrProcessingChain chain =
                DefaultOcrProcessingChain.create();

        OcrProcessingContext context =
                new OcrProcessingContext(
                        "DOC-002",
                        "small-image.jpg",
                        OcrDocumentType.IMAGE,
                        "image-content".getBytes(
                                StandardCharsets.UTF_8
                        ),
                        320,
                        480,
                        0.90D,
                        0
                );

        assertThatThrownBy(
                () -> chain.process(context)
        )
                .isInstanceOf(
                        OcrProcessingException.class
                )
                .hasMessageContaining(
                        "Image dimensions must be at least"
                );

        assertThat(context.status())
                .isEqualTo(
                        OcrProcessingStatus.REJECTED
                );

        assertThat(
                context.processingHistory()
        ).containsExactly(
                FileValidationHandler.CODE,
                DimensionValidationHandler.CODE
        );
    }

    @Test
    void shouldRejectLowQualityImageBeforePreprocessing() {
        OcrProcessingChain chain =
                DefaultOcrProcessingChain.create();

        OcrProcessingContext context =
                new OcrProcessingContext(
                        "DOC-003",
                        "blurred.png",
                        OcrDocumentType.IMAGE,
                        "image-content".getBytes(
                                StandardCharsets.UTF_8
                        ),
                        1200,
                        1600,
                        0.40D,
                        0
                );

        assertThatThrownBy(
                () -> chain.process(context)
        )
                .isInstanceOf(
                        OcrProcessingException.class
                )
                .hasMessageContaining(
                        "Image quality score must be at least"
                );

        assertThat(
                context.processingHistory()
        ).containsExactly(
                FileValidationHandler.CODE,
                DimensionValidationHandler.CODE,
                ImageQualityHandler.CODE
        );
    }
}
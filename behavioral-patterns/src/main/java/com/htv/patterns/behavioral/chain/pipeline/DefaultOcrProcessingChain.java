package com.htv.patterns.behavioral.chain.pipeline;

import com.htv.patterns.behavioral.chain.core.OcrHandler;
import com.htv.patterns.behavioral.chain.handler.DimensionValidationHandler;
import com.htv.patterns.behavioral.chain.handler.FileValidationHandler;
import com.htv.patterns.behavioral.chain.handler.ImageQualityHandler;
import com.htv.patterns.behavioral.chain.handler.NoiseReductionHandler;
import com.htv.patterns.behavioral.chain.handler.OcrReadinessHandler;
import com.htv.patterns.behavioral.chain.handler.OrientationHandler;

public final class DefaultOcrProcessingChain {

    private DefaultOcrProcessingChain() {
        throw new AssertionError(
                "Factory class must not be instantiated"
        );
    }

    public static OcrProcessingChain create() {
        OcrHandler fileValidation =
                new FileValidationHandler();

        OcrHandler dimensionValidation =
                new DimensionValidationHandler();

        OcrHandler qualityValidation =
                new ImageQualityHandler();

        OcrHandler orientation =
                new OrientationHandler();

        OcrHandler noiseReduction =
                new NoiseReductionHandler();

        OcrHandler readiness =
                new OcrReadinessHandler();

        fileValidation
                .setNext(dimensionValidation)
                .setNext(qualityValidation)
                .setNext(orientation)
                .setNext(noiseReduction)
                .setNext(readiness);

        return new OcrProcessingChain(
                fileValidation
        );
    }
}
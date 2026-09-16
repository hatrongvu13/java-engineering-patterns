package com.htv.patterns.behavioral.chain.handler;

import com.htv.patterns.behavioral.chain.core.AbstractOcrHandler;
import com.htv.patterns.behavioral.chain.core.OcrDocumentType;
import com.htv.patterns.behavioral.chain.core.OcrProcessingContext;
import com.htv.patterns.behavioral.chain.core.OcrProcessingStatus;

import java.util.Set;

public final class OrientationHandler
        extends AbstractOcrHandler {

    public static final String CODE =
            "orientation-normalization";

    private static final Set<Integer>
            SUPPORTED_ORIENTATIONS =
            Set.of(
                    0,
                    90,
                    180,
                    270
            );

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

        int orientation =
                findNearestOrientation(
                        context.orientationDegrees()
                );

        context.putAttribute(
                "originalOrientation",
                context.orientationDegrees()
        );

        context.setOrientationDegrees(
                orientation
        );

        context.putAttribute(
                "orientationNormalized",
                true
        );

        return context;
    }

    private static int findNearestOrientation(
            int degrees
    ) {
        int nearest = 0;
        int minimumDistance =
                Integer.MAX_VALUE;

        for (
                Integer candidate
                : SUPPORTED_ORIENTATIONS
        ) {
            int distance =
                    circularDistance(
                            degrees,
                            candidate
                    );

            if (distance < minimumDistance) {
                minimumDistance = distance;
                nearest = candidate;
            }
        }

        return nearest;
    }

    private static int circularDistance(
            int first,
            int second
    ) {
        int difference =
                Math.abs(first - second);

        return Math.min(
                difference,
                360 - difference
        );
    }
}
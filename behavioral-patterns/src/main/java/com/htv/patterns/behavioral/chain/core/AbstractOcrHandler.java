package com.htv.patterns.behavioral.chain.core;

import java.util.Objects;

public abstract class AbstractOcrHandler
        implements OcrHandler {

    private OcrHandler next;

    @Override
    public final OcrHandler setNext(
            OcrHandler next
    ) {
        this.next = Objects.requireNonNull(
                next,
                "next must not be null"
        );

        return next;
    }

    @Override
    public final OcrProcessingContext handle(
            OcrProcessingContext context
    ) {
        Objects.requireNonNull(
                context,
                "context must not be null"
        );

        context.addHistory(code());

        try {
            OcrProcessingContext processed =
                    Objects.requireNonNull(
                            process(context),
                            "Handler process() must not return null"
                    );

            if (next == null) {
                return processed;
            }

            return next.handle(processed);
        } catch (
                OcrProcessingException exception
        ) {
            context.setStatus(
                    OcrProcessingStatus.REJECTED
            );

            throw exception;
        } catch (
                RuntimeException exception
        ) {
            context.setStatus(
                    OcrProcessingStatus.REJECTED
            );

            throw new OcrProcessingException(
                    code(),
                    "Handler " + code()
                            + " failed: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    protected abstract OcrProcessingContext process(
            OcrProcessingContext context
    );
}
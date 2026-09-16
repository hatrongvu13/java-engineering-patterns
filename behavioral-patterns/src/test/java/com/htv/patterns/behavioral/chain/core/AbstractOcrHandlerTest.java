package com.htv.patterns.behavioral.chain.core;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AbstractOcrHandlerTest {

    @Test
    void shouldExecuteHandlersInOrder() {
        List<String> order =
                new ArrayList<>();

        OcrHandler first =
                new TrackingHandler(
                        "first",
                        order
                );

        OcrHandler second =
                new TrackingHandler(
                        "second",
                        order
                );

        OcrHandler third =
                new TrackingHandler(
                        "third",
                        order
                );

        first
                .setNext(second)
                .setNext(third);

        OcrProcessingContext result =
                first.handle(
                        validContext()
                );

        assertThat(order)
                .containsExactly(
                        "first",
                        "second",
                        "third"
                );

        assertThat(
                result.processingHistory()
        ).containsExactly(
                "first",
                "second",
                "third"
        );
    }

    @Test
    void shouldStopChainWhenHandlerFails() {
        List<String> order =
                new ArrayList<>();

        OcrHandler first =
                new TrackingHandler(
                        "first",
                        order
                );

        OcrHandler failing =
                new AbstractOcrHandler() {

                    @Override
                    public String code() {
                        return "failing";
                    }

                    @Override
                    protected OcrProcessingContext
                    process(
                            OcrProcessingContext context
                    ) {
                        order.add("failing");

                        throw new OcrProcessingException(
                                code(),
                                "Processing failed"
                        );
                    }
                };

        OcrHandler neverExecuted =
                new TrackingHandler(
                        "never-executed",
                        order
                );

        first
                .setNext(failing)
                .setNext(neverExecuted);

        OcrProcessingContext context =
                validContext();

        assertThatThrownBy(
                () -> first.handle(context)
        )
                .isInstanceOf(
                        OcrProcessingException.class
                )
                .hasMessage(
                        "Processing failed"
                );

        assertThat(order)
                .containsExactly(
                        "first",
                        "failing"
                );

        assertThat(context.status())
                .isEqualTo(
                        OcrProcessingStatus.REJECTED
                );
    }

    @Test
    void shouldRejectNullNextHandler() {
        OcrHandler handler =
                new TrackingHandler(
                        "handler",
                        new ArrayList<>()
                );

        assertThatThrownBy(
                () -> handler.setNext(null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "next must not be null"
                );
    }

    private static OcrProcessingContext
    validContext() {
        return new OcrProcessingContext(
                "DOC-001",
                "exam.png",
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

    private static final class
    TrackingHandler
            extends AbstractOcrHandler {

        private final String code;
        private final List<String> order;

        private TrackingHandler(
                String code,
                List<String> order
        ) {
            this.code = code;
            this.order = order;
        }

        @Override
        public String code() {
            return code;
        }

        @Override
        protected OcrProcessingContext process(
                OcrProcessingContext context
        ) {
            order.add(code);
            return context;
        }
    }
}
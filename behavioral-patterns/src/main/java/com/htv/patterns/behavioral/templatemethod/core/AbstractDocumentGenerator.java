package com.htv.patterns.behavioral.templatemethod.core;

import java.time.Clock;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractDocumentGenerator {

    private final DocumentStorage storage;
    private final Clock clock;

    protected AbstractDocumentGenerator(
            DocumentStorage storage
    ) {
        this(
                storage,
                Clock.systemUTC()
        );
    }

    protected AbstractDocumentGenerator(
            DocumentStorage storage,
            Clock clock
    ) {
        this.storage = Objects.requireNonNull(
                storage,
                "storage must not be null"
        );

        this.clock = Objects.requireNonNull(
                clock,
                "clock must not be null"
        );
    }

    public final DocumentResult generate(
            DocumentRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        validate(request);
        beforeLoad(request);

        DocumentData loadedData =
                requireStepResult(
                        loadData(request),
                        "loadData() must not return null"
                );

        DocumentData transformedData =
                requireStepResult(
                        transform(
                                request,
                                loadedData
                        ),
                        "transform() must not return null"
                );

        RenderedDocument document =
                requireStepResult(
                        render(
                                request,
                                transformedData
                        ),
                        "render() must not return null"
                );

        validateRenderedDocument(document);

        beforeStore(
                request,
                document
        );

        String location =
                requireText(
                        storage.store(document),
                        "DocumentStorage.store() must not return blank"
                );

        DocumentResult result =
                new DocumentResult(
                        document.format(),
                        document.fileName(),
                        document.contentType(),
                        location,
                        document.contentLength(),
                        Instant.now(clock)
                );

        afterStore(
                request,
                result
        );

        return result;
    }

    protected void validate(
            DocumentRequest request
    ) {
        if (request.parameters().isEmpty()) {
            throw new IllegalArgumentException(
                    "Document parameters must not be empty"
            );
        }
    }

    protected void beforeLoad(
            DocumentRequest request
    ) {
        // Optional hook.
    }

    protected abstract DocumentData loadData(
            DocumentRequest request
    );

    protected DocumentData transform(
            DocumentRequest request,
            DocumentData data
    ) {
        Map<String, Object> transformed =
                new LinkedHashMap<>(
                        data.values()
                );

        transformed.put(
                "language",
                request.language()
        );

        transformed.put(
                "outputName",
                request.outputName()
        );

        return new DocumentData(
                data.templateCode(),
                transformed,
                data.loadedAt()
        );
    }

    protected abstract RenderedDocument render(
            DocumentRequest request,
            DocumentData data
    );

    protected void beforeStore(
            DocumentRequest request,
            RenderedDocument document
    ) {
        // Optional hook.
    }

    protected void afterStore(
            DocumentRequest request,
            DocumentResult result
    ) {
        // Optional hook.
    }

    protected abstract DocumentFormat format();

    private void validateRenderedDocument(
            RenderedDocument document
    ) {
        if (document.format() != format()) {
            throw new IllegalStateException(
                    "Generator supports "
                            + format()
                            + " but rendered document has format "
                            + document.format()
            );
        }

        String expectedSuffix =
                "." + format().extension();

        if (
                !document.fileName()
                        .toLowerCase()
                        .endsWith(expectedSuffix)
        ) {
            throw new IllegalStateException(
                    "Rendered document file name must end with "
                            + expectedSuffix
            );
        }
    }

    private static <T> T requireStepResult(
            T result,
            String message
    ) {
        return Objects.requireNonNull(
                result,
                message
        );
    }

    private static String requireText(
            String value,
            String message
    ) {
        Objects.requireNonNull(
                value,
                message
        );

        String normalized = value.trim();

        if (normalized.isEmpty()) {
            throw new IllegalStateException(
                    message
            );
        }

        return normalized;
    }
}
package com.htv.patterns.behavioral.templatemethod.storage;

import com.htv.patterns.behavioral.templatemethod.core.DocumentStorage;
import com.htv.patterns.behavioral.templatemethod.core.RenderedDocument;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class InMemoryDocumentStorage
        implements DocumentStorage {

    private final Map<String, byte[]> documents =
            new LinkedHashMap<>();

    @Override
    public synchronized String store(
            RenderedDocument document
    ) {
        Objects.requireNonNull(
                document,
                "document must not be null"
        );

        String location =
                "memory://"
                        + UUID.randomUUID()
                        + "/"
                        + document.fileName();

        documents.put(
                location,
                document.content()
        );

        return location;
    }

    public synchronized Optional<byte[]> find(
            String location
    ) {
        Objects.requireNonNull(
                location,
                "location must not be null"
        );

        byte[] content = documents.get(location);

        if (content == null) {
            return Optional.empty();
        }

        return Optional.of(
                Arrays.copyOf(
                        content,
                        content.length
                )
        );
    }

    public synchronized boolean contains(
            String location
    ) {
        Objects.requireNonNull(
                location,
                "location must not be null"
        );

        return documents.containsKey(location);
    }

    public synchronized int size() {
        return documents.size();
    }

    public synchronized void clear() {
        documents.clear();
    }
}
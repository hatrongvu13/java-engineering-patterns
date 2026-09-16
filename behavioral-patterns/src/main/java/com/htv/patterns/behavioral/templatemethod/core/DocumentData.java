package com.htv.patterns.behavioral.templatemethod.core;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public record DocumentData(
        String templateCode,
        Map<String, Object> values,
        Instant loadedAt
) {

    public DocumentData {
        Objects.requireNonNull(
                templateCode,
                "templateCode must not be null"
        );

        values = values == null
                ? Map.of()
                : Map.copyOf(
                new LinkedHashMap<>(values)
        );

        Objects.requireNonNull(
                loadedAt,
                "loadedAt must not be null"
        );
    }
}
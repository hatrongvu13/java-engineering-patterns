package com.htv.patterns.creational.prototype.registry;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Registry of report template prototypes.
 *
 * <p>The registry stores defensive copies and always returns a new
 * deep copy to callers.</p>
 */
public final class ReportTemplateRegistry {

    private final Map<
            String,
            ReportTemplatePrototype
            > prototypes =
            new LinkedHashMap<>();

    public void register(
            String key,
            ReportTemplatePrototype prototype
    ) {
        String normalizedKey =
                normalizeKey(key);

        Objects.requireNonNull(
                prototype,
                "prototype must not be null"
        );

        if (
                prototypes.containsKey(
                        normalizedKey
                )
        ) {
            throw new DuplicatePrototypeException(
                    normalizedKey
            );
        }

        prototypes.put(
                normalizedKey,
                prototype.copy()
        );
    }

    public void replace(
            String key,
            ReportTemplatePrototype prototype
    ) {
        String normalizedKey =
                normalizeKey(key);

        Objects.requireNonNull(
                prototype,
                "prototype must not be null"
        );

        prototypes.put(
                normalizedKey,
                prototype.copy()
        );
    }

    public ReportTemplatePrototype create(
            String key
    ) {
        String normalizedKey =
                normalizeKey(key);

        ReportTemplatePrototype prototype =
                prototypes.get(normalizedKey);

        if (prototype == null) {
            throw new PrototypeNotFoundException(
                    normalizedKey
            );
        }

        return prototype.copy();
    }

    public boolean contains(
            String key
    ) {
        return prototypes.containsKey(
                normalizeKey(key)
        );
    }

    public int size() {
        return prototypes.size();
    }

    public Set<String> registeredKeys() {
        return Collections.unmodifiableSet(
                prototypes.keySet()
        );
    }

    public boolean remove(
            String key
    ) {
        return prototypes.remove(
                normalizeKey(key)
        ) != null;
    }

    private static String normalizeKey(
            String key
    ) {
        Objects.requireNonNull(
                key,
                "key must not be null"
        );

        String normalized =
                key.trim().toLowerCase();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(
                    "key must not be blank"
            );
        }

        return normalized;
    }
}
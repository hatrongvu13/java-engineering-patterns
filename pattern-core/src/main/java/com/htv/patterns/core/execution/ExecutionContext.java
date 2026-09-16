package com.htv.patterns.core.execution;

import com.htv.patterns.core.metadata.PatternCategory;

import java.time.Clock;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable description of a single pattern execution: which
 * pattern ran, in which category, when it started, and any
 * caller-supplied attributes.
 *
 * <p>Instances are created through {@link #builder(String, PatternCategory)}
 * and are safe to share across threads.
 */
public final class ExecutionContext {

    private final String patternName;
    private final PatternCategory category;
    private final Instant startedAt;
    private final Map<String, String> attributes;

    private ExecutionContext(
            Builder builder
    ) {
        this.patternName = builder.patternName;
        this.category = builder.category;
        this.startedAt = builder.startedAt;
        this.attributes = Map.copyOf(builder.attributes);
    }

    public static Builder builder(
            String patternName,
            PatternCategory category
    ) {
        return new Builder(
                patternName,
                category
        );
    }

    public String patternName() {
        return patternName;
    }

    public PatternCategory category() {
        return category;
    }

    public Instant startedAt() {
        return startedAt;
    }

    public Map<String, String> attributes() {
        return attributes;
    }

    public Optional<String> attribute(
            String key
    ) {
        return Optional.ofNullable(
                attributes.get(key)
        );
    }

    @Override
    public boolean equals(
            Object other
    ) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof ExecutionContext context)) {
            return false;
        }

        return patternName.equals(context.patternName)
                && category == context.category
                && startedAt.equals(context.startedAt)
                && attributes.equals(context.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                patternName,
                category,
                startedAt,
                attributes
        );
    }

    @Override
    public String toString() {
        return "ExecutionContext{"
                + "patternName='" + patternName + '\''
                + ", category=" + category
                + ", startedAt=" + startedAt
                + ", attributes=" + attributes
                + '}';
    }

    public static final class Builder {

        private final String patternName;
        private final PatternCategory category;
        private final Map<String, String> attributes =
                new LinkedHashMap<>();

        private Instant startedAt;
        private Clock clock = Clock.systemUTC();

        private Builder(
                String patternName,
                PatternCategory category
        ) {
            this.patternName = requireText(
                    patternName,
                    "patternName"
            );

            this.category = Objects.requireNonNull(
                    category,
                    "category must not be null"
            );
        }

        public Builder clock(
                Clock clock
        ) {
            this.clock = Objects.requireNonNull(
                    clock,
                    "clock must not be null"
            );

            return this;
        }

        public Builder startedAt(
                Instant startedAt
        ) {
            this.startedAt = Objects.requireNonNull(
                    startedAt,
                    "startedAt must not be null"
            );

            return this;
        }

        public Builder attribute(
                String key,
                String value
        ) {
            attributes.put(
                    requireText(key, "attribute key"),
                    Objects.requireNonNull(
                            value,
                            "attribute value must not be null"
                    )
            );

            return this;
        }

        public ExecutionContext build() {
            if (startedAt == null) {
                startedAt = clock.instant();
            }

            return new ExecutionContext(this);
        }

        private static String requireText(
                String value,
                String fieldName
        ) {
            Objects.requireNonNull(
                    value,
                    fieldName + " must not be null"
            );

            String normalized = value.trim();

            if (normalized.isEmpty()) {
                throw new IllegalArgumentException(
                        fieldName + " must not be blank"
                );
            }

            return normalized;
        }
    }
}

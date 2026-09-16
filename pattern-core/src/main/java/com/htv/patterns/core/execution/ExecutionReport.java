package com.htv.patterns.core.execution;

import com.htv.patterns.core.metadata.PatternStatus;
import com.htv.patterns.core.result.Result;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable outcome of an executed pattern: the originating
 * {@link ExecutionContext}, a terminal {@link PatternStatus},
 * the elapsed {@link Duration}, and an optional human-readable
 * detail message.
 */
public record ExecutionReport(
        ExecutionContext context,
        PatternStatus status,
        Duration elapsed,
        String detail
) {

    public ExecutionReport {
        Objects.requireNonNull(
                context,
                "context must not be null"
        );

        Objects.requireNonNull(
                status,
                "status must not be null"
        );

        Objects.requireNonNull(
                elapsed,
                "elapsed must not be null"
        );

        if (elapsed.isNegative()) {
            throw new IllegalArgumentException(
                    "elapsed must not be negative"
            );
        }

        detail = detail == null
                ? ""
                : detail.trim();
    }

    /**
     * Builds a report from a {@link Result}: a success maps to
     * {@link PatternStatus#COMPLETED}, a failure to
     * {@link PatternStatus#EXPERIMENTAL} with the cause message.
     */
    public static ExecutionReport from(
            ExecutionContext context,
            Result<?> result,
            Duration elapsed
    ) {
        Objects.requireNonNull(
                result,
                "result must not be null"
        );

        if (result.isSuccess()) {
            return new ExecutionReport(
                    context,
                    PatternStatus.COMPLETED,
                    elapsed,
                    "ok"
            );
        }

        return new ExecutionReport(
                context,
                PatternStatus.EXPERIMENTAL,
                elapsed,
                result.cause().getMessage()
        );
    }

    public boolean isCompleted() {
        return status == PatternStatus.COMPLETED;
    }

    public Optional<String> detailIfPresent() {
        return detail.isEmpty()
                ? Optional.empty()
                : Optional.of(detail);
    }
}

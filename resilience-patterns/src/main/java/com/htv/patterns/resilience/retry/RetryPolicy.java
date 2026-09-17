package com.htv.patterns.resilience.retry;

import java.time.Duration;
import java.util.Objects;

/**
 * Immutable retry policy: how many attempts, the initial delay,
 * and the multiplier applied to the delay after each failure
 * (exponential backoff when > 1).
 */
public record RetryPolicy(
        int maxAttempts,
        Duration initialDelay,
        double backoffMultiplier
) {

    public RetryPolicy {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException(
                    "maxAttempts must be >= 1"
            );
        }

        Objects.requireNonNull(
                initialDelay,
                "initialDelay must not be null"
        );

        if (initialDelay.isNegative()) {
            throw new IllegalArgumentException(
                    "initialDelay must not be negative"
            );
        }

        if (backoffMultiplier < 1.0) {
            throw new IllegalArgumentException(
                    "backoffMultiplier must be >= 1.0"
            );
        }
    }

    public static RetryPolicy ofAttempts(
            int maxAttempts
    ) {
        return new RetryPolicy(
                maxAttempts,
                Duration.ZERO,
                1.0
        );
    }
}

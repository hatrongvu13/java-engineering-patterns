package com.htv.patterns.resilience.circuitbreaker;

import java.time.Duration;
import java.util.Objects;

/**
 * Immutable circuit-breaker configuration: how many consecutive
 * failures trip the circuit, and how long it stays open before
 * allowing a half-open trial call.
 */
public record CircuitBreakerConfig(
        int failureThreshold,
        Duration openDuration
) {

    public CircuitBreakerConfig {
        if (failureThreshold < 1) {
            throw new IllegalArgumentException(
                    "failureThreshold must be >= 1"
            );
        }

        Objects.requireNonNull(
                openDuration,
                "openDuration must not be null"
        );

        if (openDuration.isNegative() || openDuration.isZero()) {
            throw new IllegalArgumentException(
                    "openDuration must be positive"
            );
        }
    }
}

package com.htv.patterns.resilience.circuitbreaker;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.resilience.core.ResilienceException;
import com.htv.patterns.resilience.core.ResilientOperation;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/**
 * Circuit breaker: trips OPEN after {@code failureThreshold}
 * consecutive failures, rejects calls fast while OPEN, then
 * allows one HALF_OPEN trial after {@code openDuration}. A
 * success closes the circuit; a failure re-opens it.
 *
 * <p>Not lock-free — guarded by {@code synchronized} for a clear,
 * teachable implementation.
 */
public final class CircuitBreaker {

    private final CircuitBreakerConfig config;
    private final Clock clock;

    private CircuitState state = CircuitState.CLOSED;
    private int consecutiveFailures;
    private Instant openedAt;

    public CircuitBreaker(
            CircuitBreakerConfig config
    ) {
        this(config, Clock.systemUTC());
    }

    public CircuitBreaker(
            CircuitBreakerConfig config,
            Clock clock
    ) {
        this.config = Objects.requireNonNull(
                config,
                "config must not be null"
        );

        this.clock = Objects.requireNonNull(
                clock,
                "clock must not be null"
        );
    }

    public synchronized CircuitState state() {
        return state;
    }

    public synchronized <T> Result<T> execute(
            ResilientOperation<T> operation
    ) {
        Objects.requireNonNull(
                operation,
                "operation must not be null"
        );

        if (state == CircuitState.OPEN) {
            if (cooldownElapsed()) {
                state = CircuitState.HALF_OPEN;
            } else {
                return Result.failure(
                        new ResilienceException(
                                "circuit is OPEN"
                        )
                );
            }
        }

        try {
            T value = operation.execute();
            onSuccess();
            return Result.success(value);
        } catch (Exception exception) {
            onFailure();
            return Result.failure(exception);
        }
    }

    private boolean cooldownElapsed() {
        return openedAt != null
                && !clock.instant().isBefore(
                        openedAt.plus(config.openDuration())
                );
    }

    private void onSuccess() {
        consecutiveFailures = 0;
        state = CircuitState.CLOSED;
        openedAt = null;
    }

    private void onFailure() {
        if (state == CircuitState.HALF_OPEN) {
            trip();
            return;
        }

        consecutiveFailures++;

        if (consecutiveFailures >= config.failureThreshold()) {
            trip();
        }
    }

    private void trip() {
        state = CircuitState.OPEN;
        openedAt = clock.instant();
    }
}

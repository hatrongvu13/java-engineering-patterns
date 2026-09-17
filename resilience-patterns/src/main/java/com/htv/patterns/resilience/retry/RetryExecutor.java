package com.htv.patterns.resilience.retry;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.resilience.core.ResilienceException;
import com.htv.patterns.resilience.core.ResilientOperation;

import java.time.Duration;
import java.util.Objects;

/**
 * Executes a {@link ResilientOperation} under a {@link RetryPolicy},
 * retrying on failure with (optional) exponential backoff and
 * surfacing the last failure once attempts are exhausted.
 */
public final class RetryExecutor {

    private final RetryPolicy policy;
    private final Sleeper sleeper;

    public RetryExecutor(
            RetryPolicy policy
    ) {
        this(policy, Thread::sleep);
    }

    RetryExecutor(
            RetryPolicy policy,
            Sleeper sleeper
    ) {
        this.policy = Objects.requireNonNull(
                policy,
                "policy must not be null"
        );

        this.sleeper = Objects.requireNonNull(
                sleeper,
                "sleeper must not be null"
        );
    }

    public <T> Result<T> execute(
            ResilientOperation<T> operation
    ) {
        Objects.requireNonNull(
                operation,
                "operation must not be null"
        );

        Duration delay = policy.initialDelay();
        Throwable last = null;

        for (int attempt = 1;
             attempt <= policy.maxAttempts();
             attempt++) {
            try {
                return Result.success(operation.execute());
            } catch (Exception exception) {
                last = exception;

                if (attempt < policy.maxAttempts()) {
                    sleep(delay);
                    delay = scale(delay);
                }
            }
        }

        return Result.failure(
                new ResilienceException(
                        "retries exhausted after "
                                + policy.maxAttempts()
                                + " attempts",
                        last
                )
        );
    }

    private void sleep(
            Duration delay
    ) {
        if (delay.isZero() || delay.isNegative()) {
            return;
        }

        try {
            sleeper.sleep(delay.toMillis());
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new ResilienceException(
                    "retry interrupted",
                    interrupted
            );
        }
    }

    private Duration scale(
            Duration delay
    ) {
        long scaled = Math.round(
                delay.toMillis() * policy.backoffMultiplier()
        );

        return Duration.ofMillis(scaled);
    }

    @FunctionalInterface
    interface Sleeper {
        void sleep(long millis) throws InterruptedException;
    }
}

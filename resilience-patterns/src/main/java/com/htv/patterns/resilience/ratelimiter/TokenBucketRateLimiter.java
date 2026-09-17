package com.htv.patterns.resilience.ratelimiter;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.resilience.core.ResilienceException;
import com.htv.patterns.resilience.core.ResilientOperation;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Token-bucket rate limiter: refills {@code capacity} tokens over
 * {@code refillPeriod}; each {@link #execute} consumes one token,
 * and a call with no token available is rejected fast.
 */
public final class TokenBucketRateLimiter {

    private final long capacity;
    private final Duration refillPeriod;
    private final Clock clock;

    private double tokens;
    private Instant lastRefill;

    public TokenBucketRateLimiter(
            long capacity,
            Duration refillPeriod
    ) {
        this(capacity, refillPeriod, Clock.systemUTC());
    }

    public TokenBucketRateLimiter(
            long capacity,
            Duration refillPeriod,
            Clock clock
    ) {
        if (capacity < 1) {
            throw new IllegalArgumentException(
                    "capacity must be >= 1"
            );
        }

        Objects.requireNonNull(
                refillPeriod,
                "refillPeriod must not be null"
        );

        if (refillPeriod.isNegative() || refillPeriod.isZero()) {
            throw new IllegalArgumentException(
                    "refillPeriod must be positive"
            );
        }

        this.capacity = capacity;
        this.refillPeriod = refillPeriod;
        this.clock = Objects.requireNonNull(
                clock,
                "clock must not be null"
        );
        this.tokens = capacity;
        this.lastRefill = clock.instant();
    }

    public synchronized boolean tryAcquire() {
        refill();

        if (tokens >= 1.0) {
            tokens -= 1.0;
            return true;
        }

        return false;
    }

    public synchronized <T> Result<T> execute(
            ResilientOperation<T> operation
    ) {
        Objects.requireNonNull(
                operation,
                "operation must not be null"
        );

        if (!tryAcquire()) {
            return Result.failure(
                    new ResilienceException(
                            "rate limit exceeded"
                    )
            );
        }

        try {
            return Result.success(operation.execute());
        } catch (Exception exception) {
            return Result.failure(exception);
        }
    }

    private void refill() {
        Instant now = clock.instant();
        long elapsedMillis =
                Duration.between(lastRefill, now).toMillis();

        if (elapsedMillis <= 0) {
            return;
        }

        double ratePerMilli =
                (double) capacity / refillPeriod.toMillis();

        tokens = Math.min(
                capacity,
                tokens + elapsedMillis * ratePerMilli
        );

        lastRefill = now;
    }
}

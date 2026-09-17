package com.htv.patterns.distributed.circuitbreaker;

import java.time.*;
import java.util.concurrent.Callable;

public final class CircuitBreaker {
    private final int threshold;
    private final Duration openFor;
    private final Clock clock;
    private int failures;
    private CircuitState state = CircuitState.CLOSED;
    private Instant openedAt;

    public CircuitBreaker(int threshold, Duration openFor) {
        this(threshold, openFor, Clock.systemUTC());
    }

    CircuitBreaker(int t, Duration d, Clock c) {
        threshold = t;
        openFor = d;
        clock = c;
    }

    public synchronized <T> T execute(Callable<T> action) throws Exception {
        refresh();
        if (state == CircuitState.OPEN) throw new CircuitOpenException();
        try {
            T v = action.call();
            failures = 0;
            state = CircuitState.CLOSED;
            return v;
        } catch (Exception e) {
            failures++;
            if (state == CircuitState.HALF_OPEN || failures >= threshold) {
                state = CircuitState.OPEN;
                openedAt = clock.instant();
            }
            throw e;
        }
    }

    private void refresh() {
        if (state == CircuitState.OPEN && !clock.instant().isBefore(openedAt.plus(openFor)))
            state = CircuitState.HALF_OPEN;
    }

    public synchronized CircuitState state() {
        refresh();
        return state;
    }

    public static final class CircuitOpenException extends IllegalStateException {
        public CircuitOpenException() {
            super("Circuit breaker is open");
        }
    }
}

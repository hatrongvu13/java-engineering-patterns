package com.htv.patterns.distributed.retry;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

public final class RetryPolicy {
    private final int maxAttempts;
    private final Duration delay;
    private final Predicate<Exception> retryable;

    public RetryPolicy(int maxAttempts, Duration delay, Predicate<Exception> retryable) {
        if (maxAttempts < 1) throw new IllegalArgumentException();
        this.maxAttempts = maxAttempts;
        this.delay = delay;
        this.retryable = retryable;
    }

    public <T> T execute(Callable<T> task) throws Exception {
        Exception last = null;
        for (int i = 1; i <= maxAttempts; i++) {
            try {
                return task.call();
            } catch (Exception e) {
                last = e;
                if (i == maxAttempts || !retryable.test(e)) throw e;
                if (!delay.isZero()) Thread.sleep(delay.toMillis());
            }
        }
        throw last;
    }
}

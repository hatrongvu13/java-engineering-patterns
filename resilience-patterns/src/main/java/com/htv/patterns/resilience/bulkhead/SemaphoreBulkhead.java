package com.htv.patterns.resilience.bulkhead;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.resilience.core.ResilienceException;
import com.htv.patterns.resilience.core.ResilientOperation;

import java.util.Objects;
import java.util.concurrent.Semaphore;

/**
 * Bulkhead: caps the number of concurrent executions with a
 * semaphore, so one slow dependency cannot exhaust every thread.
 * A call that cannot immediately get a permit is rejected fast.
 */
public final class SemaphoreBulkhead {

    private final int maxConcurrentCalls;
    private final Semaphore semaphore;

    public SemaphoreBulkhead(
            int maxConcurrentCalls
    ) {
        if (maxConcurrentCalls < 1) {
            throw new IllegalArgumentException(
                    "maxConcurrentCalls must be >= 1"
            );
        }

        this.maxConcurrentCalls = maxConcurrentCalls;
        this.semaphore = new Semaphore(maxConcurrentCalls);
    }

    public int availableSlots() {
        return semaphore.availablePermits();
    }

    public int maxConcurrentCalls() {
        return maxConcurrentCalls;
    }

    public <T> Result<T> execute(
            ResilientOperation<T> operation
    ) {
        Objects.requireNonNull(
                operation,
                "operation must not be null"
        );

        if (!semaphore.tryAcquire()) {
            return Result.failure(
                    new ResilienceException(
                            "bulkhead full: no free slot"
                    )
            );
        }

        try {
            return Result.success(operation.execute());
        } catch (Exception exception) {
            return Result.failure(exception);
        } finally {
            semaphore.release();
        }
    }
}

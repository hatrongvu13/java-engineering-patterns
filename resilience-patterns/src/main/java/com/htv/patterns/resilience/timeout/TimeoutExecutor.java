package com.htv.patterns.resilience.timeout;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.resilience.core.ResilienceException;
import com.htv.patterns.resilience.core.ResilientOperation;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Runs a {@link ResilientOperation} on a worker thread and
 * abandons it with a failure {@link Result} if it does not
 * complete within the configured {@link Duration}.
 */
public final class TimeoutExecutor implements AutoCloseable {

    private final Duration timeout;
    private final ExecutorService worker;

    public TimeoutExecutor(
            Duration timeout
    ) {
        this.timeout = Objects.requireNonNull(
                timeout,
                "timeout must not be null"
        );

        if (timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException(
                    "timeout must be positive"
            );
        }

        this.worker = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable, "timeout-worker");
            thread.setDaemon(true);
            return thread;
        });
    }

    public <T> Result<T> execute(
            ResilientOperation<T> operation
    ) {
        Objects.requireNonNull(
                operation,
                "operation must not be null"
        );

        Callable<T> callable = operation::execute;
        Future<T> future = worker.submit(callable);

        try {
            return Result.success(
                    future.get(
                            timeout.toMillis(),
                            TimeUnit.MILLISECONDS
                    )
            );
        } catch (TimeoutException timedOut) {
            future.cancel(true);
            return Result.failure(
                    new ResilienceException(
                            "operation timed out after "
                                    + timeout.toMillis() + "ms",
                            timedOut
                    )
            );
        } catch (ExecutionException failed) {
            return Result.failure(failed.getCause());
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            return Result.failure(
                    new ResilienceException(
                            "operation interrupted",
                            interrupted
                    )
            );
        }
    }

    @Override
    public void close() {
        worker.shutdownNow();
    }
}

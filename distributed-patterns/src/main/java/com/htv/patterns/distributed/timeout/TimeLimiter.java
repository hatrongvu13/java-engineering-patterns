package com.htv.patterns.distributed.timeout;

import java.time.Duration;
import java.util.concurrent.*;

public final class TimeLimiter implements AutoCloseable {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public <T> T execute(Callable<T> task, Duration timeout) throws Exception {
        Future<T> f = executor.submit(task);
        try {
            return f.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            f.cancel(true);
            throw e;
        } catch (ExecutionException e) {
            if (e.getCause() instanceof Exception x) throw x;
            throw e;
        }
    }

    public void close() {
        executor.shutdownNow();
    }
}

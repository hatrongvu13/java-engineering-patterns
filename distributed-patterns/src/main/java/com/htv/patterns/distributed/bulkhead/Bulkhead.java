package com.htv.patterns.distributed.bulkhead;

import java.util.concurrent.*;

public final class Bulkhead {
    private final Semaphore slots;

    public Bulkhead(int maxConcurrent) {
        slots = new Semaphore(maxConcurrent);
    }

    public <T> T execute(Callable<T> task) throws Exception {
        if (!slots.tryAcquire()) throw new RejectedExecutionException("Bulkhead capacity exhausted");
        try {
            return task.call();
        } finally {
            slots.release();
        }
    }

    public int availablePermits() {
        return slots.availablePermits();
    }
}

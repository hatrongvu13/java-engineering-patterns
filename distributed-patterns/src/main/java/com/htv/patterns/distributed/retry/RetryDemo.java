package com.htv.patterns.distributed.retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public final class RetryDemo {
    public static void main(String[] a) throws Exception {
        var n = new AtomicInteger();
        var p = new RetryPolicy(3, Duration.ZERO, e -> true);
        System.out.println(p.execute(() -> {
            if (n.incrementAndGet() < 3) throw new IllegalStateException("temporary");
            return "OK after " + n.get();
        }));
    }
}

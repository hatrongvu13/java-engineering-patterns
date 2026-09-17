package com.htv.patterns.distributed.circuitbreaker;

import java.time.Duration;

public final class CircuitBreakerDemo {
    public static void main(String[] a) {
        var c = new CircuitBreaker(2, Duration.ofSeconds(5));
        for (int i = 0; i < 3; i++)
            try {
                c.execute(() -> {
                    throw new IllegalStateException("down");
                });
            } catch (Exception e) {
                System.out.println(c.state() + ": " + e.getMessage());
            }
    }
}

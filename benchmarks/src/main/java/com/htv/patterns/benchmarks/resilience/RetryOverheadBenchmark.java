package com.htv.patterns.benchmarks.resilience;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(2)
public class RetryOverheadBenchmark {
    private int operation() {
        return 42;
    }

    private int retry() {
        for (int i = 0; i < 3; i++)
            try {
                return operation();
            } catch (RuntimeException ignored) {
            }
        throw new IllegalStateException();
    }

    @Benchmark
    public int direct() {
        return operation();
    }

    @Benchmark
    public int wrapped() {
        return retry();
    }
}

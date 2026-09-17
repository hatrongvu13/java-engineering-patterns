package com.htv.patterns.benchmarks.dispatch;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(2)
@State(Scope.Thread)
public class DispatchBenchmark {
    private final IntUnaryOperator strategy = x -> x + 1;
    @Param({"42"})
    int value;

    @Benchmark
    public int direct() {
        return value + 1;
    }

    @Benchmark
    public int strategy() {
        return strategy.applyAsInt(value);
    }
}

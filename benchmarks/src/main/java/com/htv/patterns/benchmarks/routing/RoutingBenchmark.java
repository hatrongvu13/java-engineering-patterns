package com.htv.patterns.benchmarks.routing;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.IntPredicate;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(2)
@State(Scope.Thread)
public class RoutingBenchmark {
    private final List<IntPredicate> rules = List.of(x -> x < 10, x -> x < 100, x -> true);
    @Param({"5", "50", "500"})
    int value;

    @Benchmark
    public int ifElse() {
        if (value < 10) return 0;
        if (value < 100) return 1;
        return 2;
    }

    @Benchmark
    public int ruleChain() {
        for (int i = 0; i < rules.size(); i++) if (rules.get(i).test(value)) return i;
        return -1;
    }
}

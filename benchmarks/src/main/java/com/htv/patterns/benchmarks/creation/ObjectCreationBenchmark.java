package com.htv.patterns.benchmarks.creation;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(2)
@State(Scope.Thread)
public class ObjectCreationBenchmark {
    record Item(int id, String name) {
    }

    static final class Builder {
        int id;
        String name;

        Builder id(int x) {
            id = x;
            return this;
        }

        Builder name(String x) {
            name = x;
            return this;
        }

        Item build() {
            return new Item(id, name);
        }
    }

    @Benchmark
    public Item constructor() {
        return new Item(1, "item");
    }

    @Benchmark
    public Item builder() {
        return new Builder().id(1).name("item").build();
    }
}

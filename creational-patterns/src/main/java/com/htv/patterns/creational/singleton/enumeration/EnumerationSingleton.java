package com.htv.patterns.creational.singleton.enumeration;

import java.util.concurrent.atomic.AtomicLong;

public enum EnumerationSingleton {
    INSTANCE;
    private final AtomicLong executionCount = new AtomicLong();

    public long execute() {
        return executionCount.incrementAndGet();
    }

    public long executionCount() {
        return executionCount.get();
    }

    void resetForTest() {
        executionCount.set(0);
    }
}

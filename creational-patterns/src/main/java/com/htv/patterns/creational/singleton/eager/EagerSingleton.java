package com.htv.patterns.creational.singleton.eager;

import java.util.concurrent.atomic.AtomicInteger;

public class EagerSingleton {
    private static final AtomicInteger CONSTRUCTOR_COUNT = new AtomicInteger();

    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private final long createAt;

    private EagerSingleton() {
        CONSTRUCTOR_COUNT.incrementAndGet();
        this.createAt = System.nanoTime();
    }

    public static EagerSingleton getInstance() {
        return INSTANCE;
    }

    public long getCreateAt() {
        return createAt;
    }

    static int constructorCount() {
        return CONSTRUCTOR_COUNT.get();
    }
}

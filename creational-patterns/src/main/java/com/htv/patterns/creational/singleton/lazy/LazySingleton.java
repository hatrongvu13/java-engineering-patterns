package com.htv.patterns.creational.singleton.lazy;

import java.util.concurrent.atomic.AtomicInteger;

public class LazySingleton {
    private static final AtomicInteger CONSTRUCTOR_COUNT = new AtomicInteger();

    private static LazySingleton instance;

    private final long createAt;

    private LazySingleton() {
        CONSTRUCTOR_COUNT.incrementAndGet();
        this.createAt = System.nanoTime();
    }

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }

    public long getCreateAt() {
        return createAt;
    }

    static int constructorCount() {
        return CONSTRUCTOR_COUNT.get();
    }

    static void resetForTest() {
        instance = null;
        CONSTRUCTOR_COUNT.set(0);
    }
}

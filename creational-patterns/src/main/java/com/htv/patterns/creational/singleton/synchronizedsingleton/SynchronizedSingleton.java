package com.htv.patterns.creational.singleton.synchronizedsingleton;

import java.util.concurrent.atomic.AtomicInteger;

public class SynchronizedSingleton {
    private static final AtomicInteger CONSTRUCTOR_COUNT = new AtomicInteger();

    private static SynchronizedSingleton instance;

    private final long createAt;

    private SynchronizedSingleton() {
        CONSTRUCTOR_COUNT.incrementAndGet();
        this.createAt = System.nanoTime();
    }

    public static synchronized SynchronizedSingleton getInstance() {
        if (instance == null) {
            instance = new SynchronizedSingleton();
        }
        return instance;
    }

    public long getCreateAt() {
        return createAt;
    }

    static int constructionCount() {
        return CONSTRUCTOR_COUNT.get();
    }

    static synchronized void resetForTest() {
        instance = null;
        CONSTRUCTOR_COUNT.set(0);
    }
}

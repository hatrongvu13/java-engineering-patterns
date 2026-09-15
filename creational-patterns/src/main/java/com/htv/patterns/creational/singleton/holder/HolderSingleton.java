package com.htv.patterns.creational.singleton.holder;

import java.util.concurrent.atomic.AtomicInteger;

public class HolderSingleton {
    private static final AtomicInteger CONSTRUCTOR_COUNT = new AtomicInteger();

    private final long createAt;

    private HolderSingleton() {
        CONSTRUCTOR_COUNT.incrementAndGet();
        this.createAt = System.nanoTime();
    }

    private static class InstanceHolder {
        private static final HolderSingleton INSTANCE = new HolderSingleton();
    }

    public static HolderSingleton getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public long getCreateAt() {
        return this.createAt;
    }

     static int constructionCount() {
        return CONSTRUCTOR_COUNT.get();
    }
}

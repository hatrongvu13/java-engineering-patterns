package com.htv.patterns.creational.singleton.doublechecked;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class DoubleCheckedSingleton {

    private static final AtomicInteger CONSTRUCTOR_COUNT = new AtomicInteger();

    /*
    * volatile is essential.
    *
    * Without it, another thread may observe a reference to an
    * object whose construction has not fully completed.
    */
    private static volatile DoubleCheckedSingleton instance;
    private final long createAt;
    private final String configuration;
    private DoubleCheckedSingleton() {
        CONSTRUCTOR_COUNT.incrementAndGet();
        this.createAt = System.nanoTime();
        this.configuration = "default";
    }

    public static DoubleCheckedSingleton getInstance() {
        /**
         * First check avoids synchronization after initialization.
         */
        DoubleCheckedSingleton result = instance;
        if (result == null) {
            synchronized (DoubleCheckedSingleton.class) {
                /**
                 * Second check prevents multiple constructions when several threads passed the first check
                 */
                result = instance;
                if (result == null) {
                    result = new DoubleCheckedSingleton();
                    instance = result;
                }
            }
        }

        return result;
    }

    public long getCreateAt() {
        return createAt;
    }

    public String getConfiguration() {
        return configuration;
    }

    static int constructionCount() {
        return CONSTRUCTOR_COUNT.get();
    }

    static void resetForTest() {
        synchronized (DoubleCheckedSingleton.class) {
            instance = null;
            CONSTRUCTOR_COUNT.set(0);
        }
    }
}

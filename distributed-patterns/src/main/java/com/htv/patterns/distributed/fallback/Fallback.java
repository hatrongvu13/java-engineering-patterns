package com.htv.patterns.distributed.fallback;

import java.util.concurrent.Callable;
import java.util.function.Function;

public final class Fallback {
    private Fallback() {
    }

    public static <T> T execute(Callable<T> primary, Function<Exception, T> fallback) {
        try {
            return primary.call();
        } catch (Exception e) {
            return fallback.apply(e);
        }
    }
}

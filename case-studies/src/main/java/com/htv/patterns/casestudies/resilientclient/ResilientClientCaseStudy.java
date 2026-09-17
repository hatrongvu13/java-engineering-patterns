package com.htv.patterns.casestudies.resilientclient;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ResilientClientCaseStudy {
    public String call(Callable<String> remote, String fallback) {
        var attempts = new AtomicInteger();
        while (attempts.incrementAndGet() <= 3) try {
            return remote.call();
        } catch (Exception ignored) {
        }
        return fallback;
    }
}

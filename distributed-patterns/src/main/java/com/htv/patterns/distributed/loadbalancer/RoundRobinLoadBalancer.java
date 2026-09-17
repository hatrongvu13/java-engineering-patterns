package com.htv.patterns.distributed.loadbalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class RoundRobinLoadBalancer<T> {
    private final AtomicInteger cursor = new AtomicInteger();

    public T choose(List<T> nodes) {
        if (nodes.isEmpty()) throw new IllegalStateException("No available instance");
        return nodes.get(Math.floorMod(cursor.getAndIncrement(), nodes.size()));
    }
}

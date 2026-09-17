package com.htv.patterns.distributed.sharding;

import java.util.List;
import java.util.Objects;

public final class HashShardRouter<T> {
    private final List<T> shards;

    public HashShardRouter(List<T> s) {
        if (s.isEmpty()) throw new IllegalArgumentException();
        shards = List.copyOf(s);
    }

    public T route(Object key) {
        return shards.get(Math.floorMod(Objects.hashCode(key), shards.size()));
    }
}

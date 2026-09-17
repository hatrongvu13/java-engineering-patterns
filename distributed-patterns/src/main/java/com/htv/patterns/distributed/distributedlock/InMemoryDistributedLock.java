package com.htv.patterns.distributed.distributedlock;

import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryDistributedLock {
    private record Lease(String owner, Instant expiresAt) {
    }

    private final Map<String, Lease> locks = new ConcurrentHashMap<>();
    private final Clock clock;

    public InMemoryDistributedLock() {
        this(Clock.systemUTC());
    }

    InMemoryDistributedLock(Clock c) {
        clock = c;
    }

    public synchronized boolean tryAcquire(String key, String owner, Duration ttl) {
        Lease x = locks.get(key);
        if (x != null && x.expiresAt().isAfter(clock.instant())) return false;
        locks.put(key, new Lease(owner, clock.instant().plus(ttl)));
        return true;
    }

    public synchronized boolean release(String key, String owner) {
        Lease x = locks.get(key);
        return x != null && x.owner().equals(owner) && locks.remove(key, x);
    }
}

package com.htv.patterns.distributed.leaderelection;

import java.util.Collection;

public final class BullyLeaderElection {
    public int elect(Collection<Integer> activeNodeIds) {
        return activeNodeIds.stream().mapToInt(Integer::intValue).max().orElseThrow(() -> new IllegalStateException("No active node"));
    }
}

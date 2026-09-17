package com.htv.patterns.distributed.saga;

import java.util.*;

public final class Saga {
    private final List<SagaStep> steps = new ArrayList<>();

    public Saga add(SagaStep s) {
        steps.add(s);
        return this;
    }

    public void execute() throws Exception {
        List<SagaStep> done = new ArrayList<>();
        try {
            for (var s : steps) {
                s.execute();
                done.add(s);
            }
        } catch (Exception original) {
            Collections.reverse(done);
            for (var s : done)
                try {
                    s.compensate();
                } catch (Exception compensation) {
                    original.addSuppressed(compensation);
                }
            throw original;
        }
    }
}

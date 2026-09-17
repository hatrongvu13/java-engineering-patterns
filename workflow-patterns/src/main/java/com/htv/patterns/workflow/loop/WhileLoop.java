package com.htv.patterns.workflow.loop;

import com.htv.patterns.workflow.core.*;

import java.util.function.Predicate;

public final class WhileLoop implements WorkflowStep {
    private final Predicate<WorkflowContext> condition;
    private final WorkflowStep body;
    private final int max;

    public WhileLoop(Predicate<WorkflowContext> p, WorkflowStep b, int m) {
        if (m < 1) throw new IllegalArgumentException();
        condition = p;
        body = b;
        max = m;
    }

    public void execute(WorkflowContext c) throws Exception {
        int i = 0;
        while (condition.test(c)) {
            if (++i > max) throw new IllegalStateException("Maximum workflow iterations exceeded");
            body.execute(c);
        }
    }
}
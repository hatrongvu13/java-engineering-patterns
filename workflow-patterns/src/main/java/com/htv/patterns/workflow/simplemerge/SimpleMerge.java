package com.htv.patterns.workflow.simplemerge;

import com.htv.patterns.workflow.core.*;

public final class SimpleMerge implements WorkflowStep {
    private final WorkflowStep next;

    public SimpleMerge(WorkflowStep n) {
        next = n;
    }

    public void execute(WorkflowContext c) throws Exception {
        next.execute(c);
    }
}
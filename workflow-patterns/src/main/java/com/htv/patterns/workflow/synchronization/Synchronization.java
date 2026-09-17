package com.htv.patterns.workflow.synchronization;

import com.htv.patterns.workflow.core.*;
import com.htv.patterns.workflow.parallelsplit.ParallelSplit;

import java.util.*;

public final class Synchronization implements WorkflowStep {
    private final ParallelSplit split;
    private final WorkflowStep continuation;

    public Synchronization(List<WorkflowStep> b, WorkflowStep c) {
        split = new ParallelSplit(b);
        continuation = c;
    }

    public void execute(WorkflowContext c) throws Exception {
        split.execute(c);
        continuation.execute(c);
    }
}
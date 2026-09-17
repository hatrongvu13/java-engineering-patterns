package com.htv.patterns.workflow.exclusivechoice;

import com.htv.patterns.workflow.core.*;

import java.util.*;
import java.util.function.Predicate;

public final class ExclusiveChoice implements WorkflowStep {
    private record Branch(Predicate<WorkflowContext> when, WorkflowStep step) {
    }

    private final List<Branch> branches = new ArrayList<>();
    private WorkflowStep otherwise = c -> {
    };

    public ExclusiveChoice when(Predicate<WorkflowContext> p, WorkflowStep s) {
        branches.add(new Branch(p, s));
        return this;
    }

    public ExclusiveChoice otherwise(WorkflowStep s) {
        otherwise = s;
        return this;
    }

    public void execute(WorkflowContext c) throws Exception {
        for (var b : branches)
            if (b.when.test(c)) {
                b.step.execute(c);
                return;
            }
        otherwise.execute(c);
    }
}
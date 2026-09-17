package com.htv.patterns.workflow.multichoice;

import com.htv.patterns.workflow.core.*;

import java.util.*;
import java.util.function.Predicate;

public final class MultiChoice implements WorkflowStep {
    private record Branch(Predicate<WorkflowContext> when, WorkflowStep step) {
    }

    private final List<Branch> branches = new ArrayList<>();

    public MultiChoice when(Predicate<WorkflowContext> p, WorkflowStep s) {
        branches.add(new Branch(p, s));
        return this;
    }

    public void execute(WorkflowContext c) throws Exception {
        for (var b : branches) if (b.when.test(c)) b.step.execute(c);
    }
}
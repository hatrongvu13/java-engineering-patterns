package com.htv.patterns.workflow.sequence;

import com.htv.patterns.workflow.core.*;

import java.util.*;

public final class Sequence implements WorkflowStep {
    private final List<WorkflowStep> steps;

    public Sequence(List<WorkflowStep> s) {
        steps = List.copyOf(s);
    }

    public static Sequence of(WorkflowStep... s) {
        return new Sequence(List.of(s));
    }

    public void execute(WorkflowContext c) throws Exception {
        for (var s : steps) s.execute(c);
    }
}
package com.htv.patterns.workflow.compensation;

import com.htv.patterns.workflow.core.*;

import java.util.*;

public final class CompensatingWorkflow implements WorkflowStep {
    public record Action(WorkflowStep execute, WorkflowStep compensate) {
    }

    private final List<Action> actions;

    public CompensatingWorkflow(List<Action> a) {
        actions = List.copyOf(a);
    }

    public void execute(WorkflowContext c) throws Exception {
        var done = new ArrayList<Action>();
        try {
            for (var a : actions) {
                a.execute.execute(c);
                done.add(a);
            }
        } catch (Exception original) {
            Collections.reverse(done);
            for (var a : done)
                try {
                    a.compensate.execute(c);
                } catch (Exception x) {
                    original.addSuppressed(x);
                }
            throw original;
        }
    }
}
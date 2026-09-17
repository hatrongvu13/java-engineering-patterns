package com.htv.patterns.workflow;

import com.htv.patterns.workflow.core.*;
import com.htv.patterns.workflow.sequence.Sequence;
import com.htv.patterns.workflow.exclusivechoice.ExclusiveChoice;
import com.htv.patterns.workflow.compensation.CompensatingWorkflow;
import com.htv.patterns.workflow.loop.WhileLoop;
import com.htv.patterns.workflow.state.WorkflowStateMachine;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowPatternsTest {
    @Test
    void sequencePreservesOrder() throws Exception {
        var c = new WorkflowContext();
        Sequence.of(x -> x.record("A"), x -> x.record("B")).execute(c);
        assertEquals(List.of("A", "B"), c.history());
    }

    @Test
    void exclusiveChoiceRunsOneBranch() throws Exception {
        var c = new WorkflowContext().put("ok", true);
        new ExclusiveChoice().when(x -> x.get("ok", Boolean.class), x -> x.record("yes")).otherwise(x -> x.record("no")).execute(c);
        assertEquals(List.of("yes"), c.history());
    }

    @Test
    void compensationRunsReverse() {
        var c = new WorkflowContext();
        var w = new CompensatingWorkflow(List.of(new CompensatingWorkflow.Action(x -> x.record("do-A"), x -> x.record("undo-A")), new CompensatingWorkflow.Action(x -> x.record("do-B"), x -> x.record("undo-B")), new CompensatingWorkflow.Action(x -> {
            x.record("do-C");
            throw new Exception();
        }, x -> x.record("undo-C"))));
        assertThrows(Exception.class, () -> w.execute(c));
        assertEquals(List.of("do-A", "do-B", "do-C", "undo-B", "undo-A"), c.history());
    }

    @Test
    void loopHasGuard() throws Exception {
        var c = new WorkflowContext().put("n", 0);
        new WhileLoop(x -> x.get("n", Integer.class) < 3, x -> x.put("n", x.get("n", Integer.class) + 1), 5).execute(c);
        assertEquals(3, c.get("n", Integer.class));
    }

    @Test
    void stateMachineTransitions() throws Exception {
        var c = new WorkflowContext();
        var s = new WorkflowStateMachine("DRAFT").on("DRAFT", "SUBMIT", "PENDING", x -> x.record("submitted")).on("PENDING", "APPROVE", "APPROVED", x -> x.record("approved"));
        s.fire("SUBMIT", c);
        s.fire("APPROVE", c);
        assertEquals("APPROVED", s.state());
    }
}
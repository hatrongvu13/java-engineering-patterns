package com.htv.patterns.workflow.example;

import com.htv.patterns.workflow.core.*;
import com.htv.patterns.workflow.sequence.Sequence;
import com.htv.patterns.workflow.synchronization.Synchronization;
import com.htv.patterns.workflow.exclusivechoice.ExclusiveChoice;

import java.util.List;

public final class LoanApprovalWorkflowDemo {
    public static void main(String[] a) throws Exception {
        var c = new WorkflowContext().put("score", 720).put("amount", 500_000_000L);
        WorkflowStep validate = x -> x.record("validate");
        WorkflowStep parallel = new Synchronization(List.of(x -> x.record("credit-check"), x -> x.record("fraud-check")), x -> x.record("checks-joined"));
        WorkflowStep decide = new ExclusiveChoice().when(x -> x.get("score", Integer.class) >= 700, x -> x.record("approved")).otherwise(x -> x.record("manual-review"));
        Sequence.of(validate, parallel, decide).execute(c);
        System.out.println(c.history());
    }
}
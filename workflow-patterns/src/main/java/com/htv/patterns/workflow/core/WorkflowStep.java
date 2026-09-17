package com.htv.patterns.workflow.core;

@FunctionalInterface
public interface WorkflowStep {
    void execute(WorkflowContext context) throws Exception;
}
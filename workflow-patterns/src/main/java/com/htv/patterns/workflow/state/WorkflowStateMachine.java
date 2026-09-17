package com.htv.patterns.workflow.state;

import com.htv.patterns.workflow.core.*;

import java.util.*;

public final class WorkflowStateMachine {
    private record Key(String state, String event) {
    }

    private record Transition(String target, WorkflowStep action) {
    }

    private final Map<Key, Transition> rules = new HashMap<>();
    private String state;

    public WorkflowStateMachine(String initial) {
        state = initial;
    }

    public WorkflowStateMachine on(String from, String event, String to, WorkflowStep action) {
        rules.put(new Key(from, event), new Transition(to, action));
        return this;
    }

    public synchronized void fire(String event, WorkflowContext c) throws Exception {
        var t = rules.get(new Key(state, event));
        if (t == null) throw new IllegalStateException("Invalid transition: " + state + " -> " + event);
        t.action.execute(c);
        state = t.target;
    }

    public String state() {
        return state;
    }
}
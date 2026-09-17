package com.htv.patterns.workflow.multipleinstances;

import com.htv.patterns.workflow.core.*;

import java.util.*;
import java.util.function.BiConsumer;

public final class MultipleInstances<T> implements WorkflowStep {
    private final List<T> items;
    private final BiConsumer<T, WorkflowContext> action;

    public MultipleInstances(List<T> i, BiConsumer<T, WorkflowContext> a) {
        items = List.copyOf(i);
        action = a;
    }

    public void execute(WorkflowContext c) {
        items.forEach(i -> action.accept(i, c));
    }
}
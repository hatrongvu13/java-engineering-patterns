package com.htv.patterns.workflow.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class WorkflowContext {
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private final List<String> history = Collections.synchronizedList(new ArrayList<>());

    public WorkflowContext put(String k, Object v) {
        data.put(k, v);
        return this;
    }

    public <T> T get(String k, Class<T> t) {
        return t.cast(data.get(k));
    }

    public void record(String event) {
        history.add(event);
    }

    public List<String> history() {
        return List.copyOf(history);
    }

    public Map<String, Object> snapshot() {
        return Map.copyOf(data);
    }
}
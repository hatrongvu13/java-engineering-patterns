package com.htv.patterns.workflow.cancellation;

import java.util.concurrent.atomic.AtomicBoolean;

public final class CancellationToken {
    private final AtomicBoolean cancelled = new AtomicBoolean();

    public void cancel() {
        cancelled.set(true);
    }

    public boolean isCancelled() {
        return cancelled.get();
    }

    public void throwIfCancelled() {
        if (isCancelled()) throw new WorkflowCancelledException();
    }

    public static final class WorkflowCancelledException extends RuntimeException {
        public WorkflowCancelledException() {
            super("Workflow was cancelled");
        }
    }
}
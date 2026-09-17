package com.htv.patterns.workflow.parallelsplit;
import com.htv.patterns.workflow.core.*;
import java.util.*;
import java.util.concurrent.*;
public final class ParallelSplit implements WorkflowStep {
    private final List<WorkflowStep> branches;
    public ParallelSplit(List<WorkflowStep> branches) { this.branches = List.copyOf(branches); }
    public void execute(WorkflowContext context) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(Math.max(1, branches.size()));
        try {
            List<Future<?>> futures = new ArrayList<>();
            for (WorkflowStep branch : branches) futures.add(executor.submit(() -> { branch.execute(context); return null; }));
            for (Future<?> future : futures) {
                try { future.get(); }
                catch (ExecutionException e) { if (e.getCause() instanceof Exception cause) throw cause; throw e; }
            }
        } finally { executor.shutdownNow(); }
    }
}

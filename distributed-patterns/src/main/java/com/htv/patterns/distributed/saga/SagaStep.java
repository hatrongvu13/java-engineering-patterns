package com.htv.patterns.distributed.saga;

public interface SagaStep {
    String name();

    void execute() throws Exception;

    void compensate() throws Exception;
}
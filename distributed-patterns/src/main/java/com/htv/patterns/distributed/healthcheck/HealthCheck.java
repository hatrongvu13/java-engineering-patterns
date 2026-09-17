package com.htv.patterns.distributed.healthcheck;

public interface HealthCheck {
    Result check();

    record Result(Status status, String detail) {
    }

    enum Status {UP, DOWN, DEGRADED}
}
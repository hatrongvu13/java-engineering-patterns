package com.htv.patterns.resilience.core;

/**
 * A unit of work that may fail. Resilience policies (retry,
 * circuit breaker, timeout, rate limiter, bulkhead) wrap a
 * {@code ResilientOperation} to make its execution more robust.
 *
 * @param <T> the produced value type
 */
@FunctionalInterface
public interface ResilientOperation<T> {

    T execute() throws Exception;
}

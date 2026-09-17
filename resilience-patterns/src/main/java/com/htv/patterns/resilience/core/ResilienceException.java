package com.htv.patterns.resilience.core;

/**
 * Thrown when a resilience policy refuses or aborts an execution
 * (e.g. circuit open, rate limit exceeded, bulkhead full,
 * timeout elapsed, retries exhausted).
 */
public class ResilienceException extends RuntimeException {

    public ResilienceException(
            String message
    ) {
        super(message);
    }

    public ResilienceException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}

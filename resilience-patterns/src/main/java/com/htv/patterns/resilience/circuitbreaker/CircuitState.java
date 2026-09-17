package com.htv.patterns.resilience.circuitbreaker;

/**
 * Circuit breaker states.
 *
 * <ul>
 *   <li>{@code CLOSED} — calls pass through; failures are counted.</li>
 *   <li>{@code OPEN} — calls are rejected fast until the cooldown elapses.</li>
 *   <li>{@code HALF_OPEN} — a single trial call decides whether to close or re-open.</li>
 * </ul>
 */
public enum CircuitState {

    CLOSED,
    OPEN,
    HALF_OPEN
}

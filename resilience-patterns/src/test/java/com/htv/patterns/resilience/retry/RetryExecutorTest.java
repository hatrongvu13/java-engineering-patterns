package com.htv.patterns.resilience.retry;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.resilience.core.ResilienceException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class RetryExecutorTest {

    private final List<Long> slept = new ArrayList<>();

    private RetryExecutor executor(RetryPolicy policy) {
        return new RetryExecutor(policy, slept::add);
    }

    @Test
    void shouldSucceedAfterTransientFailures() {
        AtomicInteger calls = new AtomicInteger();

        Result<String> result =
                executor(RetryPolicy.ofAttempts(3))
                        .execute(() -> {
                            if (calls.incrementAndGet() < 3) {
                                throw new IllegalStateException("x");
                            }
                            return "ok";
                        });

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.value()).isEqualTo("ok");
        assertThat(calls.get()).isEqualTo(3);
    }

    @Test
    void shouldFailWhenAttemptsExhausted() {
        Result<String> result =
                executor(RetryPolicy.ofAttempts(2))
                        .execute(() -> {
                            throw new IllegalStateException("always");
                        });

        assertThat(result.isFailure()).isTrue();
        assertThat(result.cause())
                .isInstanceOf(ResilienceException.class)
                .hasMessageContaining("retries exhausted after 2");
        assertThat(result.cause().getCause())
                .hasMessage("always");
    }

    @Test
    void shouldApplyExponentialBackoffBetweenAttempts() {
        RetryPolicy policy =
                new RetryPolicy(
                        3,
                        Duration.ofMillis(100),
                        2.0
                );

        executor(policy).execute(() -> {
            throw new IllegalStateException("x");
        });

        assertThat(slept).containsExactly(100L, 200L);
    }

    @Test
    void shouldNotSleepOnFirstSuccess() {
        executor(RetryPolicy.ofAttempts(3))
                .execute(() -> "ok");

        assertThat(slept).isEmpty();
    }
}

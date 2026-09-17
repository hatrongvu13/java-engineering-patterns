package com.htv.patterns.resilience.timeout;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.resilience.core.ResilienceException;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class TimeoutExecutorTest {

    @Test
    void shouldReturnValueWhenWithinDeadline() {
        try (TimeoutExecutor executor =
                     new TimeoutExecutor(Duration.ofSeconds(1))) {

            Result<String> result =
                    executor.execute(() -> "fast");

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.value()).isEqualTo("fast");
        }
    }

    @Test
    void shouldFailWhenOperationExceedsDeadline() {
        try (TimeoutExecutor executor =
                     new TimeoutExecutor(Duration.ofMillis(50))) {

            Result<String> result =
                    executor.execute(() -> {
                        Thread.sleep(500);
                        return "slow";
                    });

            assertThat(result.isFailure()).isTrue();
            assertThat(result.cause())
                    .isInstanceOf(ResilienceException.class)
                    .hasMessageContaining("timed out");
        }
    }

    @Test
    void shouldPropagateOperationFailure() {
        try (TimeoutExecutor executor =
                     new TimeoutExecutor(Duration.ofSeconds(1))) {

            Result<String> result =
                    executor.execute(() -> {
                        throw new IllegalStateException("boom");
                    });

            assertThat(result.isFailure()).isTrue();
            assertThat(result.cause()).hasMessage("boom");
        }
    }
}

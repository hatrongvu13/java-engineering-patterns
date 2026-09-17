package com.htv.patterns.resilience.circuitbreaker;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.resilience.core.ResilienceException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class CircuitBreakerTest {

    private final MutableClock clock =
            new MutableClock(Instant.parse("2026-01-01T00:00:00Z"));

    private final CircuitBreakerConfig config =
            new CircuitBreakerConfig(2, Duration.ofSeconds(30));

    private CircuitBreaker breaker() {
        return new CircuitBreaker(config, clock);
    }

    @Test
    void shouldTripOpenAfterThresholdFailures() {
        CircuitBreaker breaker = breaker();

        breaker.execute(this::fail);
        assertThat(breaker.state()).isEqualTo(CircuitState.CLOSED);

        breaker.execute(this::fail);
        assertThat(breaker.state()).isEqualTo(CircuitState.OPEN);
    }

    @Test
    void shouldRejectFastWhileOpen() {
        CircuitBreaker breaker = breaker();
        breaker.execute(this::fail);
        breaker.execute(this::fail);

        Result<String> result = breaker.execute(() -> "ok");

        assertThat(result.isFailure()).isTrue();
        assertThat(result.cause())
                .isInstanceOf(ResilienceException.class)
                .hasMessage("circuit is OPEN");
    }

    @Test
    void shouldCloseAfterSuccessfulHalfOpenTrial() {
        CircuitBreaker breaker = breaker();
        breaker.execute(this::fail);
        breaker.execute(this::fail);

        clock.advance(Duration.ofSeconds(31));

        Result<String> result = breaker.execute(() -> "ok");

        assertThat(result.isSuccess()).isTrue();
        assertThat(breaker.state()).isEqualTo(CircuitState.CLOSED);
    }

    @Test
    void shouldReopenWhenHalfOpenTrialFails() {
        CircuitBreaker breaker = breaker();
        breaker.execute(this::fail);
        breaker.execute(this::fail);

        clock.advance(Duration.ofSeconds(31));
        breaker.execute(this::fail);

        assertThat(breaker.state()).isEqualTo(CircuitState.OPEN);
    }

    private String fail() {
        throw new IllegalStateException("boom");
    }

    private static final class MutableClock extends Clock {

        private Instant now;

        private MutableClock(Instant start) {
            this.now = start;
        }

        void advance(Duration by) {
            now = now.plus(by);
        }

        @Override
        public Instant instant() {
            return now;
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }
    }
}

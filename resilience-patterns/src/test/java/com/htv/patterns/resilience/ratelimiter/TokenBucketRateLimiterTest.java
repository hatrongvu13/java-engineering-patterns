package com.htv.patterns.resilience.ratelimiter;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.resilience.core.ResilienceException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class TokenBucketRateLimiterTest {

    private final MutableClock clock =
            new MutableClock(Instant.parse("2026-01-01T00:00:00Z"));

    @Test
    void shouldAllowUpToCapacityThenReject() {
        TokenBucketRateLimiter limiter =
                new TokenBucketRateLimiter(
                        2,
                        Duration.ofSeconds(1),
                        clock
                );

        assertThat(limiter.tryAcquire()).isTrue();
        assertThat(limiter.tryAcquire()).isTrue();
        assertThat(limiter.tryAcquire()).isFalse();
    }

    @Test
    void shouldRefillOverTime() {
        TokenBucketRateLimiter limiter =
                new TokenBucketRateLimiter(
                        2,
                        Duration.ofSeconds(1),
                        clock
                );

        limiter.tryAcquire();
        limiter.tryAcquire();
        assertThat(limiter.tryAcquire()).isFalse();

        clock.advance(Duration.ofSeconds(1));

        assertThat(limiter.tryAcquire()).isTrue();
        assertThat(limiter.tryAcquire()).isTrue();
    }

    @Test
    void executeShouldFailFastWhenExhausted() {
        TokenBucketRateLimiter limiter =
                new TokenBucketRateLimiter(
                        1,
                        Duration.ofSeconds(1),
                        clock
                );

        assertThat(limiter.execute(() -> "ok").value())
                .isEqualTo("ok");

        Result<String> rejected =
                limiter.execute(() -> "ok");

        assertThat(rejected.isFailure()).isTrue();
        assertThat(rejected.cause())
                .isInstanceOf(ResilienceException.class)
                .hasMessage("rate limit exceeded");
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
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }
    }
}

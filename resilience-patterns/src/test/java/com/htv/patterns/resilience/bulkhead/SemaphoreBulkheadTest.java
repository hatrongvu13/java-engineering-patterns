package com.htv.patterns.resilience.bulkhead;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.resilience.core.ResilienceException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class SemaphoreBulkheadTest {

    @Test
    void shouldReleaseSlotAfterExecution() {
        SemaphoreBulkhead bulkhead = new SemaphoreBulkhead(2);

        Result<String> result =
                bulkhead.execute(() -> "ok");

        assertThat(result.value()).isEqualTo("ok");
        assertThat(bulkhead.availableSlots()).isEqualTo(2);
    }

    @Test
    void shouldRejectWhenSaturated() throws InterruptedException {
        SemaphoreBulkhead bulkhead = new SemaphoreBulkhead(1);

        CountDownLatch inside = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);

        Thread holder = new Thread(() ->
                bulkhead.execute(() -> {
                    inside.countDown();
                    release.await();
                    return "held";
                })
        );
        holder.start();
        inside.await();

        Result<String> rejected =
                bulkhead.execute(() -> "second");

        assertThat(rejected.isFailure()).isTrue();
        assertThat(rejected.cause())
                .isInstanceOf(ResilienceException.class)
                .hasMessageContaining("bulkhead full");

        release.countDown();
        holder.join();
        assertThat(bulkhead.availableSlots()).isEqualTo(1);
    }

    @Test
    void shouldReleaseSlotOnFailure() {
        SemaphoreBulkhead bulkhead = new SemaphoreBulkhead(1);

        Result<String> result =
                bulkhead.execute(() -> {
                    throw new IllegalStateException("boom");
                });

        assertThat(result.isFailure()).isTrue();
        assertThat(bulkhead.availableSlots()).isEqualTo(1);
    }
}

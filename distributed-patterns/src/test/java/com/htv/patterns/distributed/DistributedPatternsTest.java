package com.htv.patterns.distributed;

import com.htv.patterns.distributed.bulkhead.Bulkhead;
import com.htv.patterns.distributed.circuitbreaker.*;
import com.htv.patterns.distributed.idempotentconsumer.IdempotentConsumer;
import com.htv.patterns.distributed.loadbalancer.RoundRobinLoadBalancer;
import com.htv.patterns.distributed.outbox.*;
import com.htv.patterns.distributed.retry.RetryPolicy;
import com.htv.patterns.distributed.saga.*;
import com.htv.patterns.distributed.sharding.HashShardRouter;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class DistributedPatternsTest {
    @Test
    void retryEventuallySucceeds() throws Exception {
        var n = new AtomicInteger();
        assertEquals("ok", new RetryPolicy(3, Duration.ZERO, e -> true).execute(() -> {
            if (n.incrementAndGet() < 3) throw new IllegalStateException();
            return "ok";
        }));
        assertEquals(3, n.get());
    }

    @Test
    void circuitOpensAtThreshold() {
        var c = new CircuitBreaker(2, Duration.ofMinutes(1));
        for (int i = 0; i < 2; i++)
            assertThrows(Exception.class, () -> c.execute(() -> {
                throw new Exception("down");
            }));
        assertEquals(CircuitState.OPEN, c.state());
        assertThrows(CircuitBreaker.CircuitOpenException.class, () -> c.execute(() -> "never"));
    }

    @Test
    void roundRobinCycles() {
        var b = new RoundRobinLoadBalancer<String>();
        assertEquals("A", b.choose(List.of("A", "B")));
        assertEquals("B", b.choose(List.of("A", "B")));
        assertEquals("A", b.choose(List.of("A", "B")));
    }

    @Test
    void sagaCompensatesInReverse() {
        var log = new ArrayList<String>();
        var saga = new Saga().add(step("A", log, false)).add(step("B", log, false)).add(step("C", log, true));
        assertThrows(Exception.class, saga::execute);
        assertEquals(List.of("do-A", "do-B", "do-C", "undo-B", "undo-A"), log);
    }

    private SagaStep step(String n, List<String> log, boolean fail) {
        return new SagaStep() {
            public String name() {
                return n;
            }

            public void execute() throws Exception {
                log.add("do-" + n);
                if (fail) throw new Exception("failed");
            }

            public void compensate() {
                log.add("undo-" + n);
            }
        };
    }

    @Test
    void outboxPublishesAndRemoves() {
        var o = new InMemoryOutbox();
        o.save(OutboxEvent.of("OrderCreated", "{}"));
        assertEquals(1, o.publishPending(e -> {
        }));
        assertEquals(0, o.size());
    }

    @Test
    void idempotentConsumerRejectsDuplicate() {
        var n = new AtomicInteger();
        var c = new IdempotentConsumer<String>();
        assertTrue(c.accept("1", "x", x -> n.incrementAndGet()));
        assertFalse(c.accept("1", "x", x -> n.incrementAndGet()));
        assertEquals(1, n.get());
    }

    @Test
    void shardingIsStable() {
        var r = new HashShardRouter<>(List.of("S1", "S2", "S3"));
        assertEquals(r.route("customer-1"), r.route("customer-1"));
    }

    @Test
    void bulkheadReleasesPermit() throws Exception {
        var b = new Bulkhead(1);
        assertEquals("ok", b.execute(() -> "ok"));
        assertEquals(1, b.availablePermits());
    }
}

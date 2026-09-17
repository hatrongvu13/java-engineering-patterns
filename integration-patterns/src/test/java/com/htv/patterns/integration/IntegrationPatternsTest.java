package com.htv.patterns.integration;

import com.htv.patterns.integration.channel.*;
import com.htv.patterns.integration.core.Message;
import com.htv.patterns.integration.endpoint.IdempotentReceiver;
import com.htv.patterns.integration.routing.*;
import com.htv.patterns.integration.transformation.MessageTranslator;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationPatternsTest {
    @Test
    void contentBasedRouterChoosesMatchingChannel() {
        var high = new PointToPointChannel<Integer>();
        var low = new PointToPointChannel<Integer>();
        new ContentBasedRouter<Integer>().when(m -> m.payload() > 10, high).otherwise(low).route(Message.of(20));
        assertEquals(1, high.size());
        assertEquals(0, low.size());
    }

    @Test
    void splitterAndAggregatorRoundTrip() {
        var parts = new Splitter<String, String>(s -> List.of(s.split(","))).apply(Message.of("A,B,C"));
        var aggregator = new Aggregator<String, String>(xs -> xs.stream().sorted(Comparator.comparingInt(m -> (int) m.headers().get("sequenceNumber"))).map(Message::payload).reduce("", String::concat));
        assertTrue(aggregator.add(parts.get(0)).isEmpty());
        assertTrue(aggregator.add(parts.get(1)).isEmpty());
        assertEquals("ABC", aggregator.add(parts.get(2)).orElseThrow().payload());
    }

    @Test
    void idempotentReceiverIgnoresDuplicate() {
        var calls = new AtomicInteger();
        var receiver = new IdempotentReceiver<String>(m -> calls.incrementAndGet());
        var message = Message.of("event");
        receiver.handle(message);
        receiver.handle(message);
        assertEquals(1, calls.get());
    }

    @Test
    void translatorPreservesMessageIdentity() {
        var source = Message.of("42");
        var result = new MessageTranslator<String, Integer>(Integer::parseInt).translate(source);
        assertEquals(source.id(), result.id());
        assertEquals(42, result.payload());
    }
}

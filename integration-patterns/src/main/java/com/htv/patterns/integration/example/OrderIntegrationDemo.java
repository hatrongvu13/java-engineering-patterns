package com.htv.patterns.integration.example;

import com.htv.patterns.integration.channel.*;
import com.htv.patterns.integration.core.Message;
import com.htv.patterns.integration.routing.ContentBasedRouter;

public final class OrderIntegrationDemo {
    public record Order(String id, long amount) {
    }

    public static void main(String[] args) {
        var normal = new PointToPointChannel<Order>();
        var priority = new PointToPointChannel<Order>();
        var router = new ContentBasedRouter<Order>().when(m -> m.payload().amount() >= 100_000_000, priority).otherwise(normal);
        router.route(Message.of(new Order("ORD-001", 150_000_000)));
        System.out.println("Priority queue size: " + priority.size());
    }
}

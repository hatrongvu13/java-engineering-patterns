package com.htv.patterns.distributed.example;

import com.htv.patterns.distributed.common.ServiceInstance;
import com.htv.patterns.distributed.serviceregistry.InMemoryServiceRegistry;
import com.htv.patterns.distributed.servicediscovery.ServiceDiscovery;

import java.net.URI;

public final class DistributedPatternsDemo {
    public static void main(String[] a) {
        var r = new InMemoryServiceRegistry();
        r.register(new ServiceInstance("order-1", "order-service", URI.create("http://localhost:8081")));
        r.register(new ServiceInstance("order-2", "order-service", URI.create("http://localhost:8082")));
        var d = new ServiceDiscovery(r);
        System.out.println(d.resolve("order-service"));
        System.out.println(d.resolve("order-service"));
    }
}

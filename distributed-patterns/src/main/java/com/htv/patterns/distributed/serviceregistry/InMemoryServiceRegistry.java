package com.htv.patterns.distributed.serviceregistry;

import com.htv.patterns.distributed.common.ServiceInstance;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryServiceRegistry implements ServiceRegistry {
    private final Map<String, ServiceInstance> data = new ConcurrentHashMap<>();

    public void register(ServiceInstance i) {
        data.put(i.id(), i);
    }

    public void deregister(String id) {
        data.remove(id);
    }

    public List<ServiceInstance> find(String name) {
        return data.values().stream().filter(x -> x.serviceName().equals(name)).sorted(Comparator.comparing(ServiceInstance::id)).toList();
    }
}

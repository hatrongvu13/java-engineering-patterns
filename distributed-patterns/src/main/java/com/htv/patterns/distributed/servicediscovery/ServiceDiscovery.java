package com.htv.patterns.distributed.servicediscovery;

import com.htv.patterns.distributed.common.ServiceInstance;
import com.htv.patterns.distributed.loadbalancer.RoundRobinLoadBalancer;
import com.htv.patterns.distributed.serviceregistry.ServiceRegistry;

public final class ServiceDiscovery {
    private final ServiceRegistry registry;
    private final RoundRobinLoadBalancer<ServiceInstance> balancer = new RoundRobinLoadBalancer<>();

    public ServiceDiscovery(ServiceRegistry r) {
        registry = r;
    }

    public ServiceInstance resolve(String name) {
        return balancer.choose(registry.find(name));
    }
}

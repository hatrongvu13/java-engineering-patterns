package com.htv.patterns.distributed.serviceregistry;

import com.htv.patterns.distributed.common.ServiceInstance;

import java.util.List;

public interface ServiceRegistry {
    void register(ServiceInstance i);

    void deregister(String id);

    List<ServiceInstance> find(String serviceName);
}

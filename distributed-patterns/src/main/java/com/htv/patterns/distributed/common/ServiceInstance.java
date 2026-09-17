package com.htv.patterns.distributed.common;

import java.net.URI;
import java.util.Objects;

public record ServiceInstance(String id, String serviceName, URI endpoint) {
    public ServiceInstance {
        Objects.requireNonNull(id);
        Objects.requireNonNull(serviceName);
        Objects.requireNonNull(endpoint);
    }
}

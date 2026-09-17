# Project Structure

```text
com.htv.patterns.distributed
├── common.common
├── retry.retry
├── circuitbreaker.circuitbreaker
├── timeout.timeout
├── fallback.fallback
├── bulkhead.bulkhead
├── serviceregistry.serviceregistry
├── servicediscovery.servicediscovery
├── loadbalancer.loadbalancer
├── saga.saga
├── outbox.outbox
├── idempotentconsumer.idempotentconsumer
├── leaderelection.leaderelection
├── distributedlock.distributedlock
├── sharding.sharding
├── healthcheck.healthcheck
└── example.example
```

Mỗi pattern nằm trong package riêng và có API tối thiểu. Demo có `main`; hành vi quan trọng được xác nhận ở `DistributedPatternsTest`.

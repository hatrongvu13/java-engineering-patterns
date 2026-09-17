# Mục lục Pattern

Liên kết tới README chi tiết của **mọi pattern** trong dự án, nhóm
theo module. Mỗi README có mô tả, "khi nào dùng", sơ đồ PlantUML
(class + sequence) và lưu ý triển khai.

Xem thêm: [Hướng dẫn chọn pattern](pattern-selection-guide.md) ·
[Kiến trúc](architecture.md) · [Tổng quan module](architecture/module-overview.md)

## Creational — tạo đối tượng

- [Singleton](../creational-patterns/src/main/java/com/htv/patterns/creational/singleton/README.md)
- [Builder](../creational-patterns/src/main/java/com/htv/patterns/creational/builder/README.md)
- [Factory](../creational-patterns/src/main/java/com/htv/patterns/creational/factory/README.md)
- [Abstract Factory](../creational-patterns/src/main/java/com/htv/patterns/creational/factory/abstractfactory/README.md)
- [Factory Registry](../creational-patterns/src/main/java/com/htv/patterns/creational/factory/registry/README.md)
- [Prototype](../creational-patterns/src/main/java/com/htv/patterns/creational/prototype/README.md)

## Behavioral — hành vi / tương tác

- [Strategy](../behavioral-patterns/src/main/java/com/htv/patterns/behavioral/strategy/README.md)
- [Chain of Responsibility](../behavioral-patterns/src/main/java/com/htv/patterns/behavioral/chain/README.md)
- [Template Method](../behavioral-patterns/src/main/java/com/htv/patterns/behavioral/templatemethod/README.md)
- [Observer](../behavioral-patterns/src/main/java/com/htv/patterns/behavioral/observer/README.md)

## Structural — cấu trúc / kết hợp

- [Adapter](../structural-patterns/src/main/java/com/htv/patterns/structural/adapter/README.md)
- [Decorator](../structural-patterns/src/main/java/com/htv/patterns/structural/decorator/README.md)
- [Proxy](../structural-patterns/src/main/java/com/htv/patterns/structural/proxy/README.md)
- [Facade](../structural-patterns/src/main/java/com/htv/patterns/structural/facade/README.md)
- [Composite](../structural-patterns/src/main/java/com/htv/patterns/structural/composite/README.md)
- [Bridge](../structural-patterns/src/main/java/com/htv/patterns/structural/bridge/README.md)
- [Flyweight](../structural-patterns/src/main/java/com/htv/patterns/structural/flyweight/README.md)

## Resilience — chịu lỗi / ổn định

- [Retry](../resilience-patterns/src/main/java/com/htv/patterns/resilience/retry/README.md)
- [Circuit Breaker](../resilience-patterns/src/main/java/com/htv/patterns/resilience/circuitbreaker/README.md)
- [Timeout](../resilience-patterns/src/main/java/com/htv/patterns/resilience/timeout/README.md)
- [Rate Limiter](../resilience-patterns/src/main/java/com/htv/patterns/resilience/ratelimiter/README.md)
- [Bulkhead](../resilience-patterns/src/main/java/com/htv/patterns/resilience/bulkhead/README.md)

## Integration — tích hợp / messaging

- [Core (Message / Handler)](../integration-patterns/src/main/java/com/htv/patterns/integration/core/README.md)
- [Message Channel](../integration-patterns/src/main/java/com/htv/patterns/integration/channel/README.md)
- [Messaging Endpoints](../integration-patterns/src/main/java/com/htv/patterns/integration/endpoint/README.md)
- [Message Routing](../integration-patterns/src/main/java/com/htv/patterns/integration/routing/README.md)
- [Message Transformation](../integration-patterns/src/main/java/com/htv/patterns/integration/transformation/README.md)
- [Example (Order Integration)](../integration-patterns/src/main/java/com/htv/patterns/integration/example/README.md)

## Distributed — hệ phân tán

- [Saga](../distributed-patterns/src/main/java/com/htv/patterns/distributed/saga/README.md)
- [Transactional Outbox](../distributed-patterns/src/main/java/com/htv/patterns/distributed/outbox/README.md)
- [Idempotent Consumer](../distributed-patterns/src/main/java/com/htv/patterns/distributed/idempotentconsumer/README.md)
- [Leader Election](../distributed-patterns/src/main/java/com/htv/patterns/distributed/leaderelection/README.md)
- [Distributed Lock](../distributed-patterns/src/main/java/com/htv/patterns/distributed/distributedlock/README.md)
- [Service Registry](../distributed-patterns/src/main/java/com/htv/patterns/distributed/serviceregistry/README.md)
- [Service Discovery](../distributed-patterns/src/main/java/com/htv/patterns/distributed/servicediscovery/README.md)
- [Load Balancer](../distributed-patterns/src/main/java/com/htv/patterns/distributed/loadbalancer/README.md)
- [Sharding](../distributed-patterns/src/main/java/com/htv/patterns/distributed/sharding/README.md)
- [Circuit Breaker](../distributed-patterns/src/main/java/com/htv/patterns/distributed/circuitbreaker/README.md)
- [Bulkhead](../distributed-patterns/src/main/java/com/htv/patterns/distributed/bulkhead/README.md)
- [Retry](../distributed-patterns/src/main/java/com/htv/patterns/distributed/retry/README.md)
- [Timeout / Time Limiter](../distributed-patterns/src/main/java/com/htv/patterns/distributed/timeout/README.md)
- [Fallback](../distributed-patterns/src/main/java/com/htv/patterns/distributed/fallback/README.md)
- [Health Check](../distributed-patterns/src/main/java/com/htv/patterns/distributed/healthcheck/README.md)

## Workflow — điều khiển luồng

- [Core (Step / Context)](../workflow-patterns/src/main/java/com/htv/patterns/workflow/core/README.md)
- [Sequence](../workflow-patterns/src/main/java/com/htv/patterns/workflow/sequence/README.md)
- [Parallel Split](../workflow-patterns/src/main/java/com/htv/patterns/workflow/parallelsplit/README.md)
- [Synchronization](../workflow-patterns/src/main/java/com/htv/patterns/workflow/synchronization/README.md)
- [Exclusive Choice](../workflow-patterns/src/main/java/com/htv/patterns/workflow/exclusivechoice/README.md)
- [Simple Merge](../workflow-patterns/src/main/java/com/htv/patterns/workflow/simplemerge/README.md)
- [Multi-Choice](../workflow-patterns/src/main/java/com/htv/patterns/workflow/multichoice/README.md)
- [Multiple Instances](../workflow-patterns/src/main/java/com/htv/patterns/workflow/multipleinstances/README.md)
- [Loop (While)](../workflow-patterns/src/main/java/com/htv/patterns/workflow/loop/README.md)
- [State Machine](../workflow-patterns/src/main/java/com/htv/patterns/workflow/state/README.md)
- [Cancellation](../workflow-patterns/src/main/java/com/htv/patterns/workflow/cancellation/README.md)
- [Compensation](../workflow-patterns/src/main/java/com/htv/patterns/workflow/compensation/README.md)
- [Example (Loan Approval)](../workflow-patterns/src/main/java/com/htv/patterns/workflow/example/README.md)

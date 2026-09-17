# Tổng quan module

| Module | Nội dung | Phụ thuộc | Trạng thái |
|---|---|---|---|
| `pattern-core` | `Result<T>`, `ExecutionContext`, `ExecutionReport`, enum metadata | — | Done |
| `creational-patterns` | Singleton (8 biến thể), Factory (simple/method/abstract/registry), Builder (classic/fluent/step/validated), Prototype (shallow/deep/copy-ctor/registry) | `pattern-core` | Done |
| `behavioral-patterns` | Strategy, Chain of Responsibility, Template Method, Observer | `pattern-core`, POI | Done |
| `structural-patterns` | Adapter, Decorator, Proxy, Facade, Composite, Bridge, Flyweight | `pattern-core` | Done |
| `resilience-patterns` | Retry, Circuit Breaker, Timeout, Rate Limiter, Bulkhead | `pattern-core` | Done |
| `integration-patterns` | Messaging / gateway (dự kiến) | `pattern-core` | Stub |
| `distributed-patterns` | Saga, Outbox, Idempotency (dự kiến) | `pattern-core` | Stub |
| `workflow-patterns` | Điều phối workflow (dự kiến) | `pattern-core` | Stub |
| `case-studies` | Kịch bản đầu-cuối ghép nhiều pattern | tất cả | Stub |
| `benchmarks` | Micro-benchmark (JMH-style) | tất cả | Stub |

"Stub" = chỉ còn `App.java` Hello World + test JUnit 3, chưa triển khai.

## Sơ đồ phụ thuộc module

```puml
component "pattern-core" as core

component "creational-patterns" as creational
component "behavioral-patterns" as behavioral
component "structural-patterns" as structural
component "resilience-patterns" as resilience
component "integration-patterns" as integration
component "distributed-patterns" as distributed
component "workflow-patterns" as workflow
component "case-studies" as cases
component "benchmarks" as bench

creational --> core
behavioral --> core
structural --> core
resilience --> core
integration --> core
distributed --> core
workflow --> core

cases --> creational
cases --> behavioral
cases --> structural
cases --> resilience
bench --> core
```

Chi tiết luồng dữ liệu và tầng nền: xem `docs/architecture.md`.

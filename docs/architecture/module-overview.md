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

Chi tiết luồng dữ liệu và tầng nền: xem `docs/architecture.md`.

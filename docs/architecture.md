# Kiến trúc hệ thống

## Tổng quan

`java-engineering-patterns` là một dự án Maven **đa module**, mỗi
module gom một nhóm design pattern theo phân loại kỹ thuật. Tất cả
module đều phụ thuộc vào `pattern-core` — tầng nền chứa các primitive
dùng chung.

## Sơ đồ phụ thuộc module

```
pattern-core
    ▲
    ├── creational-patterns
    ├── behavioral-patterns
    ├── structural-patterns
    ├── resilience-patterns
    ├── integration-patterns   (stub)
    ├── distributed-patterns   (stub)
    └── workflow-patterns      (stub)

tất cả pattern modules
    ▲
case-studies   (stub)   ·   benchmarks   (stub)
```

Quy tắc phụ thuộc: module pattern chỉ được phụ thuộc `pattern-core`,
không phụ thuộc lẫn nhau. `case-studies` mới được phép ghép nhiều
module để dựng kịch bản đầu-cuối.

## Các module chính

| Module | Vai trò | Trạng thái |
|---|---|---|
| `pattern-core` | `Result<T>`, `ExecutionContext`, `ExecutionReport`, enum metadata | Done |
| `creational-patterns` | Singleton, Factory, Builder, Prototype | Done |
| `behavioral-patterns` | Strategy, Chain of Responsibility, Template Method, Observer | Done |
| `structural-patterns` | Adapter, Decorator, Proxy, Facade, Composite, Bridge, Flyweight | Done |
| `resilience-patterns` | Retry, Circuit Breaker, Timeout, Rate Limiter, Bulkhead | Done |
| `integration/distributed/workflow-patterns`, `case-studies`, `benchmarks` | Chưa triển khai (stub) | Planned |

## Tầng nền `pattern-core`

- **`Result<T>`** — kiểu kết quả functional dạng `sealed` (`Success`/`Failure`), có `map`/`flatMap`/`onSuccess`/`onFailure`/`orElse`/`toOptional`. Mọi API public trả `Result` thay vì ném exception qua ranh giới, giúp caller xử lý lỗi tường minh.
- **`ExecutionContext`** — mô tả bất biến một lần chạy pattern (tên, category, thời điểm, attributes); tạo qua `Builder`, `Clock` inject được.
- **`ExecutionReport`** — `record` kết quả chạy: context + `PatternStatus` + `Duration` + detail; có `from(context, result, elapsed)`.
- **Enum metadata** — `PatternCategory`, `PatternStatus`.

## Luồng dữ liệu tiêu biểu

Ví dụ luồng thanh toán trong `structural-patterns` (ghép nhiều pattern):

```
CheckoutRequest
   │
   ▼
CheckoutFacade (Facade)
   │  1. quy đổi tiền tệ
   ├─────────────► ExchangeRateService (Proxy cache)
   │                     └─► RemoteExchangeRateService
   │  2. chọn gateway theo provider
   ▼
PaymentGateway (Adapter)  ── StripeGatewayAdapter → StripeClient
   │                          PaypalGatewayAdapter → PaypalClient
   │  (có thể bọc thêm Decorator: Logging / Retry)
   ▼
Result<ChargeReceipt>
```

Luồng resilience: một `ResilientOperation<T>` được bọc bởi các policy
(Bulkhead → RateLimiter → CircuitBreaker → Retry → Timeout), mỗi lớp
trả `Result<T>`; lỗi do policy từ chối là `ResilienceException`.

## Nguyên tắc thiết kế xuyên suốt

- Value object là `record`, validate trong compact constructor.
- Bất biến + `final` mặc định; phụ thuộc tiêm qua constructor.
- `Clock` inject được cho code phụ thuộc thời gian → test tất định.
- Không ném exception qua API public — dùng `Result<T>`.

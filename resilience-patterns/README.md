# Resilience Patterns

Patterns that keep a system responsive and stable when a
dependency is slow, failing, or overloaded. Every policy wraps a
`ResilientOperation<T>` and returns a
`com.htv.patterns.core.result.Result<T>` instead of throwing
across boundaries, so callers compose failures explicitly.

## Catalog

| Pattern | Class | Bảo vệ chống | Ứng dụng điển hình |
|---|---|---|---|
| Retry | `retry.RetryExecutor` | Lỗi thoáng qua (transient) | Gọi API mạng chập chờn, deadlock DB tạm thời |
| Circuit Breaker | `circuitbreaker.CircuitBreaker` | Lỗi kéo dài / dây chuyền | Ngắt nhanh khi dependency chết, tránh dồn tải |
| Timeout | `timeout.TimeoutExecutor` | Treo / phản hồi chậm | Chặn thread bị giữ vô hạn bởi call chậm |
| Rate Limiter | `ratelimiter.TokenBucketRateLimiter` | Quá tải / lạm dụng | Giới hạn QPS tới API bên thứ ba, chống spam |
| Bulkhead | `bulkhead.SemaphoreBulkhead` | Cạn kiệt tài nguyên | Cô lập pool thread cho từng dependency |

Status: **Completed** — mỗi pattern có code, test, và README riêng.

## Kết hợp các pattern (thứ tự khuyến nghị)

Khi xếp chồng nhiều policy quanh một lời gọi từ xa, thứ tự từ
ngoài vào trong thường là:

```
Bulkhead → RateLimiter → CircuitBreaker → Retry → Timeout → operation
```

- **Bulkhead / RateLimiter** ở ngoài cùng: chặn tải trước khi tốn tài nguyên.
- **CircuitBreaker** trước **Retry**: khi mạch mở, không retry vô ích.
- **Timeout** trong cùng: mỗi lần thử (kể cả retry) đều bị chặn thời gian.

## Conventions

- Tham số cấu hình là `record` bất biến, validate trong compact constructor.
- Thời gian dùng `java.time.Clock` inject được → test không phụ thuộc đồng hồ thật.
- Kết quả trả `Result<T>`; lỗi do policy từ chối là `ResilienceException`.
- Test JUnit 5 + AssertJ, đặt tên `should…`, có ca thất bại.

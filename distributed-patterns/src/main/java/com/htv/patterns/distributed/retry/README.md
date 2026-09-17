# Retry (distributed)

Thử lại lời gọi từ xa lỗi thoáng qua theo `RetryPolicy`, giúp vượt
qua lỗi mạng tạm thời.

## Khi nào dùng

- Lỗi mạng tạm thời (timeout lẻ, 503) khi gọi service khác.
- Không dùng cho lỗi xác định (4xx nghiệp vụ).

## Cấu trúc

```puml
classDiagram
    class RetryPolicy {
        +maxAttempts()
        +backoff()
    }
```

## Lưu ý triển khai

- Kết hợp backoff để tránh dồn tải service đang hồi phục.
- Nên đặt sau Circuit Breaker (mạch mở thì không retry). Xem `resilience-patterns/.../retry`.

# Circuit Breaker (distributed)

Ngắt nhanh lời gọi tới một dịch vụ từ xa đang lỗi kéo dài, tránh
dồn tải và lỗi dây chuyền giữa các service.

## Khi nào dùng

- Gọi service phụ thuộc qua mạng, service đó có thể chết/quá tải.
- Cần "fail fast" thay vì chờ timeout lặp lại.

## Cấu trúc

```puml
classDiagram
    class CircuitBreaker {
        +execute(operation) Result
        +state() CircuitState
    }
    class CircuitState {
        <<enum>>
        CLOSED
        OPEN
        HALF_OPEN
    }
    CircuitBreaker --> CircuitState
```

## Lưu ý triển khai

- Chuyển CLOSED → OPEN sau ngưỡng lỗi; HALF_OPEN thử lại sau cooldown.
- Xem thêm bản đầy đủ ở `resilience-patterns/.../circuitbreaker` (cùng cơ chế, ngữ cảnh nội tiến trình).

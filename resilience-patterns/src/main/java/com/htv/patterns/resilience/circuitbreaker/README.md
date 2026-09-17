# Circuit Breaker

Ngắt nhanh khi một dependency lỗi kéo dài: sau `failureThreshold`
lần lỗi liên tiếp, mạch chuyển OPEN và từ chối mọi call trong
`openDuration`; sau đó cho một call thử (HALF_OPEN). Thành công thì
đóng mạch, thất bại thì mở lại.

## Khi nào dùng

- Dependency **chết/quá tải kéo dài** — retry vô ích chỉ làm nặng thêm.
- Cần "fail fast" để giải phóng thread và giữ hệ thống phản hồi.

## Trạng thái

```
CLOSED --(đủ ngưỡng lỗi)--> OPEN --(hết cooldown)--> HALF_OPEN
  ▲                                                     │
  └────────────(trial thành công)──────────────────────┘
                       (trial lỗi) → OPEN
```

## Cấu trúc

```puml
classDiagram
    class CircuitState {
        <<enum>>
        CLOSED
        OPEN
        HALF_OPEN
    }

    class CircuitBreakerConfig {
        +int failureThreshold
        +Duration openDuration
    }

    class CircuitBreaker {
        -CircuitState state
        -Clock clock
        +execute(ResilientOperation) Result
        +state() CircuitState
    }

    CircuitBreaker --> CircuitBreakerConfig
    CircuitBreaker --> CircuitState
```

## Lưu ý triển khai

- `Clock` inject được → test điều khiển thời gian cooldown, không sleep thật.
- Guard bằng `synchronized` cho rõ ràng, dễ dạy (chưa tối ưu lock-free).

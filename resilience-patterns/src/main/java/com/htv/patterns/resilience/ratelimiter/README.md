# Rate Limiter

Giới hạn tần suất bằng **token bucket**: bình chứa tối đa
`capacity` token, được nạp đều trong `refillPeriod`; mỗi lần gọi
tiêu 1 token, hết token thì từ chối nhanh.

## Khi nào dùng

- Bảo vệ API bên thứ ba khỏi vượt hạn mức (QPS).
- Chống spam / lạm dụng endpoint.
- Làm mượt burst traffic.

## Cấu trúc

```puml
classDiagram
    class TokenBucketRateLimiter {
        -long capacity
        -Duration refillPeriod
        -double tokens
        -Clock clock
        +tryAcquire() boolean
        +execute(ResilientOperation) Result
    }
```

```puml
sequenceDiagram
    participant Client
    participant RL as TokenBucketRateLimiter

    Client->>RL: execute(op)
    RL->>RL: refill theo thời gian trôi
    alt còn token
        RL->>RL: tokens -= 1
        RL-->>Client: Result.success(...)
    else hết token
        RL-->>Client: Result.failure("rate limit exceeded")
    end
```

## Lưu ý triển khai

- Nạp token liên tục: `tokens += elapsedMillis * capacity / refillPeriodMillis` (chặn trần ở `capacity`).
- `Clock` inject được → test refill tất định, không chờ thật.
- Guard bằng `synchronized`.

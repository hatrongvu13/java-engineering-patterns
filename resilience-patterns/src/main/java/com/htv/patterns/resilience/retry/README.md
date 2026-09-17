# Retry

Thử lại một thao tác lỗi thoáng qua theo `RetryPolicy` (số lần thử,
delay ban đầu, hệ số backoff). Hết lượt thì trả `Result.failure`
bọc `ResilienceException` với nguyên nhân cuối.

## Khi nào dùng

- Lỗi **tạm thời, tự khỏi**: timeout mạng lẻ, HTTP 503, deadlock DB nhất thời.
- **Không** dùng cho lỗi xác định (400, validation) — retry chỉ tốn công.

## Cấu trúc

```puml
classDiagram
    class RetryPolicy {
        +int maxAttempts
        +Duration initialDelay
        +double backoffMultiplier
        +ofAttempts(int) RetryPolicy
    }

    class RetryExecutor {
        -RetryPolicy policy
        -Sleeper sleeper
        +execute(ResilientOperation) Result
    }

    RetryExecutor --> RetryPolicy
```

```puml
sequenceDiagram
    participant Client
    participant Retry as RetryExecutor
    participant Op as ResilientOperation

    Client->>Retry: execute(op)
    Retry->>Op: execute()  %% attempt 1
    Op-->>Retry: throw
    Retry->>Retry: sleep(delay); delay *= multiplier
    Retry->>Op: execute()  %% attempt 2
    Op-->>Retry: value
    Retry-->>Client: Result.success(value)
```

## Lưu ý triển khai

- `Sleeper` được inject → test kiểm tra chuỗi delay mà không ngủ thật.
- Backoff: `delay(n) = initialDelay * multiplier^(n-1)`.

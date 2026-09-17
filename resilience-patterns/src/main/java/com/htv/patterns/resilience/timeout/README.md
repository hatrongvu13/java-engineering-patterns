# Timeout

Chạy thao tác trên một worker thread và bỏ dở (trả `Result.failure`)
nếu không hoàn thành trong `Duration` cho trước.

## Khi nào dùng

- Call ngoài có nguy cơ **treo / phản hồi rất chậm**, giữ thread vô hạn.
- Cần chặn thời gian cho từng lần thử (đặt trong cùng khi kết hợp Retry).

## Cấu trúc

```puml
classDiagram
    class TimeoutExecutor {
        -Duration timeout
        -ExecutorService worker
        +execute(ResilientOperation) Result
        +close()
    }
    TimeoutExecutor ..|> AutoCloseable
```

```puml
sequenceDiagram
    participant Client
    participant TE as TimeoutExecutor
    participant W as worker thread

    Client->>TE: execute(op)
    TE->>W: submit(op)
    alt hoàn thành đúng hạn
        W-->>TE: value
        TE-->>Client: Result.success(value)
    else quá hạn
        TE->>W: future.cancel(true)
        TE-->>Client: Result.failure(ResilienceException "timed out")
    end
```

## Lưu ý triển khai

- Worker là daemon thread pool → không chặn JVM thoát.
- `TimeoutExecutor` là `AutoCloseable`; dùng try-with-resources để `shutdownNow`.
- Timeout **không** đảm bảo thao tác đã bị dừng thật (chỉ ngừng chờ) — thao tác cần tôn trọng interrupt.

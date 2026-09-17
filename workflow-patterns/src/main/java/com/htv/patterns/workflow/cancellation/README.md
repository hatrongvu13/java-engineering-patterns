# Cancellation

Hủy một luồng đang chạy một cách hợp tác qua `CancellationToken`,
để các bước kiểm tra và dừng sớm an toàn.

## Khi nào dùng

- Luồng dài cần cho phép hủy giữa chừng (người dùng hủy, timeout ngoài).

## Cấu trúc

```puml
classDiagram
    class CancellationToken {
        +cancel() void
        +isCancelled() boolean
        +throwIfCancelled() void
    }
```

```puml
sequenceDiagram
    participant Caller
    participant Token as CancellationToken
    participant Step

    Caller->>Token: cancel()
    Step->>Token: throwIfCancelled()
    Token-->>Step: dừng (đã hủy)
```

## Lưu ý triển khai

- Hủy **hợp tác**: các bước phải chủ động kiểm tra token, không cưỡng bức dừng thread.

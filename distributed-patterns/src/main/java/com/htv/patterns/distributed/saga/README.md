# Saga

Điều phối một giao dịch phân tán thành chuỗi bước cục bộ, mỗi bước
có hành động **bù trừ** (compensation) để hoàn tác khi một bước sau
thất bại — thay cho giao dịch ACID phân tán.

## Khi nào dùng

- Nghiệp vụ trải nhiều service/DB, không thể dùng một transaction chung.
- Chấp nhận nhất quán cuối (eventual consistency) + rollback nghiệp vụ.

## Cấu trúc

```puml
classDiagram
    class Saga {
        +addStep(SagaStep) Saga
        +execute() Result
    }
    class SagaStep {
        <<interface>>
        +action() void
        +compensate() void
    }
    Saga o-- SagaStep : orchestrates
```

```puml
sequenceDiagram
    participant Saga
    participant S1 as Step 1
    participant S2 as Step 2
    participant S3 as Step 3

    Saga->>S1: action()
    Saga->>S2: action()
    Saga->>S3: action()  %% lỗi
    S3-->>Saga: fail
    Saga->>S2: compensate()  %% hoàn tác ngược
    Saga->>S1: compensate()
```

## Lưu ý triển khai

- `Saga` chạy các `SagaStep` theo thứ tự; lỗi thì gọi `compensate()` ngược lại cho các bước đã hoàn thành.
- Kết quả trả `Result` — không ném exception qua ranh giới.
- Bù trừ nên **idempotent** (có thể chạy lại an toàn).

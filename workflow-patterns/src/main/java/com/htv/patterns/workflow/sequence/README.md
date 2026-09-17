# Sequence

Chạy các bước tuần tự, mỗi bước nhận context từ bước trước — khối
điều khiển luồng cơ bản nhất.

## Khi nào dùng

- Các bước phụ thuộc thứ tự, chạy lần lượt A → B → C.

## Cấu trúc

```puml
classDiagram
    class Sequence {
        +then(WorkflowStep) Sequence
        +execute(WorkflowContext) WorkflowContext
    }
    class WorkflowStep {
        <<interface>>
    }
    Sequence o-- WorkflowStep
```

```puml
sequenceDiagram
    participant Seq as Sequence
    participant A
    participant B

    Seq->>A: execute(ctx)
    A-->>Seq: ctx1
    Seq->>B: execute(ctx1)
    B-->>Seq: ctx2
```

## Lưu ý triển khai

- Context truyền nối tiếp giữa các bước (immutable, tạo bản mới mỗi bước).

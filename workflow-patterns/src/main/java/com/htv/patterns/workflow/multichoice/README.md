# Multi-Choice

Chọn **một hoặc nhiều** nhánh cùng thoả điều kiện để chạy (OR-split).

## Khi nào dùng

- Nhiều nhánh có thể cùng đúng và cùng cần chạy (khác XOR chỉ một).

## Cấu trúc

```puml
classDiagram
    class MultiChoice {
        +when(predicate, WorkflowStep) MultiChoice
        +execute(WorkflowContext) List
    }
    class WorkflowStep {
        <<interface>>
    }
    MultiChoice o-- WorkflowStep
```

```puml
sequenceDiagram
    participant MC as MultiChoice
    participant B1
    participant B2

    MC->>MC: đánh giá mọi điều kiện
    MC->>B1: execute(ctx)  %% B1 khớp
    MC->>B2: execute(ctx)  %% B2 cũng khớp
```

## Lưu ý triển khai

- Chạy **mọi** nhánh có predicate đúng; kết quả nhiều nhánh thường gộp bằng Synchronization/Merge.

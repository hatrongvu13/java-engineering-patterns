# Exclusive Choice

Chọn **đúng một** nhánh đi tiếp dựa trên điều kiện (XOR-split), như
lệnh if/switch trong luồng.

## Khi nào dùng

- Rẽ nhánh loại trừ: chỉ một đường được chọn theo điều kiện.

## Cấu trúc

```puml
classDiagram
    class ExclusiveChoice {
        +when(predicate, WorkflowStep) ExclusiveChoice
        +otherwise(WorkflowStep) ExclusiveChoice
        +execute(WorkflowContext) WorkflowContext
    }
    class WorkflowStep {
        <<interface>>
    }
    ExclusiveChoice o-- WorkflowStep
```

```puml
sequenceDiagram
    participant EC as ExclusiveChoice
    participant Branch

    EC->>EC: đánh giá điều kiện theo thứ tự
    EC->>Branch: execute(ctx)  %% nhánh đầu tiên khớp
    Branch-->>EC: ctx'
```

## Lưu ý triển khai

- Đánh giá predicate theo thứ tự, chọn nhánh khớp đầu tiên; có nhánh `otherwise` mặc định.

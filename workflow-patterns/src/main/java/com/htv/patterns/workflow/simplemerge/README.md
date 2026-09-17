# Simple Merge

Gộp nhiều nhánh loại trừ về một luồng mà **không** đồng bộ hoá
(XOR-join) — chỉ một nhánh từng chạy nên không cần chờ.

## Khi nào dùng

- Sau Exclusive Choice / Multi-Choice, hợp các đường về một điểm tiếp theo.

## Cấu trúc

```puml
classDiagram
    class SimpleMerge {
        +merge(WorkflowContext) WorkflowContext
        +then(WorkflowStep) SimpleMerge
    }
```

```puml
sequenceDiagram
    participant Branch
    participant M as SimpleMerge
    participant Next

    Branch-->>M: ctx (nhánh được chọn)
    M->>Next: tiếp tục
```

## Lưu ý triển khai

- Không chờ nhiều nhánh (khác Synchronization) — giả định chỉ một nhánh đến.

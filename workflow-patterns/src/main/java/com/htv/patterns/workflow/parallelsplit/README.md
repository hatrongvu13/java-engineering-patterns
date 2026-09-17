# Parallel Split

Tách luồng thành nhiều nhánh chạy đồng thời từ một điểm (AND-split).

## Khi nào dùng

- Nhiều công việc độc lập có thể chạy song song sau một điểm.
- Thường đi kèm Synchronization để gộp các nhánh lại.

## Cấu trúc

```puml
classDiagram
    class ParallelSplit {
        +addBranch(WorkflowStep) ParallelSplit
        +execute(WorkflowContext) List
    }
    class WorkflowStep {
        <<interface>>
    }
    ParallelSplit o-- WorkflowStep : branches
```

```puml
sequenceDiagram
    participant PS as ParallelSplit
    participant B1 as Branch 1
    participant B2 as Branch 2

    PS->>B1: execute(ctx)
    PS->>B2: execute(ctx)
    B1-->>PS: ctx1
    B2-->>PS: ctx2
```

## Lưu ý triển khai

- Các nhánh khởi chạy từ cùng một context đầu vào.
- Kết quả nhiều nhánh cần Synchronization để hợp nhất.

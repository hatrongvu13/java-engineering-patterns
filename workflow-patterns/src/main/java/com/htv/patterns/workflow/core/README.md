# Workflow Core

Nền tảng cho các workflow control-flow pattern: `WorkflowStep`
(một bước xử lý) và `WorkflowContext` (trạng thái luồng chảy qua
các bước).

## Khi nào dùng

- Là abstraction chung mọi pattern điều khiển luồng dựng lên.

## Cấu trúc

```puml
classDiagram
    class WorkflowStep {
        <<interface>>
        +execute(WorkflowContext) WorkflowContext
    }
    class WorkflowContext {
        +get(key)
        +with(key, value) WorkflowContext
    }
    WorkflowStep ..> WorkflowContext
```

## Lưu ý triển khai

- `WorkflowContext` bất biến: mỗi bước trả context mới thay vì sửa tại chỗ.
- `WorkflowStep` là functional interface — nền cho sequence, split, loop, choice...

# Loop (While)

Lặp một bước khi điều kiện còn đúng (structured loop / while).

## Khi nào dùng

- Cần lặp một xử lý cho đến khi thoả điều kiện dừng (retry nghiệp vụ, polling).

## Cấu trúc

```puml
classDiagram
    class WhileLoop {
        +WhileLoop(predicate, WorkflowStep)
        +execute(WorkflowContext) WorkflowContext
    }
    class WorkflowStep {
        <<interface>>
    }
    WhileLoop o-- WorkflowStep
```

```puml
sequenceDiagram
    participant L as WhileLoop
    participant Step

    loop trong khi predicate(ctx) đúng
        L->>Step: execute(ctx)
        Step-->>L: ctx'
    end
    L-->>L: thoát khi điều kiện sai
```

## Lưu ý triển khai

- Điều kiện đánh giá trên context hiện tại trước mỗi vòng.
- Cần đảm bảo điều kiện dừng để tránh vòng lặp vô hạn.

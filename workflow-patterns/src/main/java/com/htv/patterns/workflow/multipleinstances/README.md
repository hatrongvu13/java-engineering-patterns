# Multiple Instances

Chạy nhiều thể hiện của cùng một bước trên một tập phần tử (ví dụ
xử lý từng dòng đơn hàng).

## Khi nào dùng

- Cùng một xử lý áp cho nhiều item (fan-out theo dữ liệu).

## Cấu trúc

```puml
classDiagram
    class MultipleInstances {
        +forEach(items, WorkflowStep) List
    }
    class WorkflowStep {
        <<interface>>
    }
    MultipleInstances ..> WorkflowStep
```

```puml
sequenceDiagram
    participant MI as MultipleInstances
    participant Step

    loop mỗi item
        MI->>Step: execute(ctx với item)
        Step-->>MI: ctx'
    end
```

## Lưu ý triển khai

- Sinh một instance bước cho mỗi phần tử; kết quả gom thành danh sách.

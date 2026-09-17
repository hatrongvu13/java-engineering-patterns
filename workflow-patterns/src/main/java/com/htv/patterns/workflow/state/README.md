# State Machine

Điều khiển luồng theo máy trạng thái: các trạng thái và chuyển tiếp
hợp lệ giữa chúng.

## Khi nào dùng

- Luồng có tập trạng thái rõ ràng và chuyển tiếp có ràng buộc (đơn hàng, phê duyệt).
- Cần chặn chuyển tiếp không hợp lệ.

## Cấu trúc

```puml
classDiagram
    class WorkflowStateMachine {
        +addTransition(from, event, to) WorkflowStateMachine
        +fire(event) Result
        +currentState()
    }
```

```puml
stateDiagram-v2
    [*] --> DRAFT
    DRAFT --> SUBMITTED : submit
    SUBMITTED --> APPROVED : approve
    SUBMITTED --> REJECTED : reject
```

## Lưu ý triển khai

- Chỉ cho phép chuyển tiếp đã khai báo; sự kiện không hợp lệ trả `Result.failure`.
- Trạng thái hiện tại được giữ trong máy; chuyển tiếp là tất định theo (state, event).

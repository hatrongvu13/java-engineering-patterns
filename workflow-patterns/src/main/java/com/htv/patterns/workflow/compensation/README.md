# Compensation

Hoàn tác các bước đã hoàn thành khi luồng thất bại về sau — phiên
bản workflow của bù trừ kiểu Saga.

## Khi nào dùng

- Luồng nhiều bước có hiệu ứng phụ cần rollback nghiệp vụ khi lỗi.

## Cấu trúc

```puml
classDiagram
    class CompensatingWorkflow {
        +addStep(action, compensation) CompensatingWorkflow
        +execute(WorkflowContext) Result
    }
```

```puml
sequenceDiagram
    participant W as CompensatingWorkflow
    participant S1
    participant S2

    W->>S1: action()
    W->>S2: action()  %% lỗi
    S2-->>W: fail
    W->>S1: compensate()  %% hoàn tác bước đã xong
    W-->>W: Result.failure
```

## Lưu ý triển khai

- Ghi lại các bước đã chạy; lỗi thì gọi bù trừ ngược thứ tự.
- Liên hệ chặt với `distributed/saga`; ở đây là trong một luồng workflow.

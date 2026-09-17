# Idempotent Consumer

Đảm bảo xử lý mỗi message đúng một lần về mặt hiệu ứng, kể cả khi
message được giao lại (at-least-once delivery).

## Khi nào dùng

- Broker giao ít nhất một lần → có thể trùng.
- Thao tác không tự nhiên idempotent (trừ tiền, tạo đơn...).

## Cấu trúc

```puml
classDiagram
    class IdempotentConsumer {
        +process(messageId, action) Result
        +hasProcessed(messageId) boolean
    }
```

```puml
sequenceDiagram
    participant Broker
    participant IC as IdempotentConsumer
    participant Handler

    Broker->>IC: process(#7, action)
    IC->>Handler: action.run()  %% lần đầu
    IC->>IC: ghi nhận #7
    Broker->>IC: process(#7, action)  %% giao lại
    IC-->>Broker: bỏ qua (#7 đã xử lý)
```

## Lưu ý triển khai

- Lưu tập id đã xử lý; kiểm tra trước khi chạy action.
- Kết quả trả `Result`; việc ghi nhận id + chạy action cần an toàn với truy cập đồng thời.

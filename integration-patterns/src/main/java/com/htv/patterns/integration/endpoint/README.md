# Messaging Endpoints

Điểm cuối kết nối ứng dụng với kênh: xử lý cạnh tranh nhiều
consumer và chống xử lý trùng.

## Khi nào dùng

- **Competing Consumers**: nhiều worker cùng rút từ một kênh để tăng thông lượng.
- **Idempotent Receiver**: message có thể tới trùng (at-least-once) và cần bỏ trùng.

## Cấu trúc

```puml
classDiagram
    class CompetingConsumers
    class IdempotentReceiver
    class MessageHandler~T~ {
        <<interface>>
    }
    CompetingConsumers ..> MessageHandler
    IdempotentReceiver ..|> MessageHandler
```

```puml
sequenceDiagram
    participant Ch as Channel
    participant IR as IdempotentReceiver
    participant App

    Ch->>IR: handle(message #42)
    IR->>App: xử lý (lần đầu)
    Ch->>IR: handle(message #42)   %% trùng
    IR-->>Ch: bỏ qua (đã thấy #42)
```

## Lưu ý triển khai

- `CompetingConsumers` phân phối tải cho nhiều handler song song.
- `IdempotentReceiver` lưu id message đã xử lý để loại bản trùng.

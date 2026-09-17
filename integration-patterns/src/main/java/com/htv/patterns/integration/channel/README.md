# Message Channel

Kênh vận chuyển `Message` giữa producer và consumer. Hai kiểu:
điểm-điểm (một consumer nhận) và publish-subscribe (mọi subscriber
nhận).

## Khi nào dùng

- Tách producer khỏi consumer qua một kênh trung gian.
- Point-to-point: mỗi message chỉ một bên xử lý (hàng đợi công việc).
- Publish-subscribe: phát broadcast cho nhiều subscriber (sự kiện).

## Cấu trúc

```puml
classDiagram
    class MessageChannel~T~ {
        <<interface>>
        +send(Message~T~) void
        +subscribe(MessageHandler~T~) void
    }
    class PointToPointChannel
    class PublishSubscribeChannel

    MessageChannel <|.. PointToPointChannel
    MessageChannel <|.. PublishSubscribeChannel
```

```puml
sequenceDiagram
    participant P as Producer
    participant Ch as PublishSubscribeChannel
    participant S1 as Subscriber A
    participant S2 as Subscriber B

    S1->>Ch: subscribe(handler)
    S2->>Ch: subscribe(handler)
    P->>Ch: send(message)
    Ch->>S1: handle(message)
    Ch->>S2: handle(message)
```

## Lưu ý triển khai

- `PointToPointChannel`: giao message cho đúng một consumer.
- `PublishSubscribeChannel`: fan-out tới mọi subscriber đã đăng ký.

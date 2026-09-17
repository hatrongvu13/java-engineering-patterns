# Observer

Phát sự kiện miền (domain event) cho nhiều listener đăng ký, tách
bên phát khỏi bên nhận. Kịch bản: theo dõi vòng đời xử lý OCR
(`OcrProcessingStarted/Completed/Failed`).

## Khi nào dùng

- Một hành động cần thông báo **nhiều bên** mà không biết trước là ai.
- Muốn nới lỏng ghép nối giữa nơi sinh sự kiện và nơi xử lý.

## Cấu trúc

```puml
classDiagram
    class DomainEvent {
        <<interface>>
    }
    class DomainEventListener~E~ {
        <<interface>>
        +onEvent(E) void
    }
    class DomainEventPublisher {
        <<interface>>
        +publish(DomainEvent) EventPublicationResult
        +subscribe(...) EventListenerSubscription
    }
    class SimpleDomainEventPublisher
    class ListenerFailurePolicy {
        <<enum>>
    }
    class EventPublicationResult
    class OcrProcessingStarted
    class OcrProcessingCompleted
    class OcrProcessingFailed

    DomainEventPublisher <|.. SimpleDomainEventPublisher
    DomainEvent <|.. OcrProcessingStarted
    DomainEvent <|.. OcrProcessingCompleted
    DomainEvent <|.. OcrProcessingFailed
    SimpleDomainEventPublisher --> ListenerFailurePolicy
    SimpleDomainEventPublisher --> EventPublicationResult
    SimpleDomainEventPublisher o-- DomainEventListener
```

```puml
sequenceDiagram
    participant Producer
    participant Publisher as SimpleDomainEventPublisher
    participant L1 as Listener A
    participant L2 as Listener B

    Producer->>Publisher: publish(OcrProcessingCompleted)
    Publisher->>L1: onEvent(event)
    Publisher->>L2: onEvent(event)
    Note over Publisher: lỗi listener xử lý theo ListenerFailurePolicy
    Publisher-->>Producer: EventPublicationResult
```

## Lưu ý triển khai

- `ListenerFailurePolicy` quyết định: một listener lỗi thì dừng hay tiếp tục phát cho các listener còn lại.
- `EventPublicationResult` gom kết quả từng listener; lỗi bọc trong `EventPublicationException`.
- `EventListenerSubscription` cho phép huỷ đăng ký (unsubscribe).

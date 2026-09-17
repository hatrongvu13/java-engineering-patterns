# Transactional Outbox

Ghi sự kiện vào một "outbox" trong **cùng transaction** với thay đổi
nghiệp vụ, rồi phát đi sau — đảm bảo không mất/không lệch giữa cập
nhật DB và publish message.

## Khi nào dùng

- Cần "cập nhật DB" và "phát event" nguyên tử mà không có 2PC.
- Chống mất event khi service crash giữa hai thao tác.

## Cấu trúc

```puml
classDiagram
    class OutboxEvent {
        +id()
        +payload()
        +occurredAt()
    }
    class InMemoryOutbox {
        +append(OutboxEvent) void
        +pollUnpublished() List
        +markPublished(id) void
    }
    InMemoryOutbox o-- OutboxEvent
```

```puml
sequenceDiagram
    participant Tx as Business Tx
    participant Outbox
    participant Relay
    participant Broker

    Tx->>Outbox: append(event)  %% cùng transaction
    Relay->>Outbox: pollUnpublished()
    Relay->>Broker: publish(event)
    Relay->>Outbox: markPublished(id)
```

## Lưu ý triển khai

- `OutboxEvent` là record bất biến.
- `InMemoryOutbox` mô phỏng bảng outbox; relay poll các event chưa publish rồi đánh dấu đã gửi.
- Consumer phía sau nên idempotent vì relay có thể gửi trùng (at-least-once).

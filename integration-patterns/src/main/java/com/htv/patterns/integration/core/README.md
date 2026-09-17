# Integration Core

Nền tảng messaging cho các Enterprise Integration Patterns: đơn vị
`Message` bất biến và `MessageHandler` xử lý nó.

## Khi nào dùng

- Là abstraction chung mọi pattern messaging khác dựng lên.
- Khi cần một envelope thống nhất cho payload + metadata.

## Cấu trúc

```puml
classDiagram
    class Message~T~ {
        +payload() T
        +headers() Map
    }
    class MessageHandler~T~ {
        <<interface>>
        +handle(Message~T~) void
    }
    MessageHandler ..> Message : consumes
```

## Lưu ý triển khai

- `Message` là value object bất biến (record), header sao chép phòng thủ.
- `MessageHandler` là functional interface — nền cho channel, router, transformer.

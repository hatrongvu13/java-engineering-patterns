# Message Routing

Điều hướng và tái cấu trúc luồng message: lọc, định tuyến theo nội
dung, tách/gộp, và gửi tới danh sách người nhận.

## Khi nào dùng

- **ContentBasedRouter**: chọn đích theo nội dung message.
- **MessageFilter**: loại message không thoả điều kiện.
- **Splitter / Aggregator**: tách một message lớn thành nhiều phần và gộp kết quả lại.
- **RecipientList**: gửi cùng message tới nhiều đích xác định.

## Cấu trúc

```puml
classDiagram
    class ContentBasedRouter
    class MessageFilter
    class Splitter
    class Aggregator
    class RecipientList
    class MessageChannel~T~ {
        <<interface>>
    }

    ContentBasedRouter --> MessageChannel : routes to
    RecipientList --> MessageChannel : fans to
    Splitter ..> Aggregator : split then aggregate
```

```puml
sequenceDiagram
    participant In
    participant Split as Splitter
    participant Agg as Aggregator
    participant Out

    In->>Split: message [a,b,c]
    Split->>Agg: a
    Split->>Agg: b
    Split->>Agg: c
    Agg-->>Out: gộp kết quả (a+b+c)
```

## Lưu ý triển khai

- `ContentBasedRouter` quyết định `MessageChannel` đích dựa trên predicate/nội dung.
- `Aggregator` gom theo correlation id đến khi đủ điều kiện hoàn tất.
- `Splitter` sinh nhiều message con từ một message tổng.

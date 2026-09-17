# Synchronization

Gộp nhiều nhánh song song về một luồng, chờ tất cả hoàn thành
(AND-join) trước khi tiếp tục.

## Khi nào dùng

- Sau Parallel Split, cần chờ mọi nhánh xong rồi mới đi tiếp.

## Cấu trúc

```puml
classDiagram
    class Synchronization {
        +join(List~WorkflowContext~) WorkflowContext
    }
```

```puml
sequenceDiagram
    participant B1 as Branch 1
    participant B2 as Branch 2
    participant S as Synchronization
    participant Next

    B1-->>S: ctx1 (xong)
    B2-->>S: ctx2 (xong)
    S->>S: chờ đủ mọi nhánh
    S->>Next: context hợp nhất
```

## Lưu ý triển khai

- Chỉ tiếp tục khi **tất cả** nhánh hoàn thành (AND-join).
- Hợp nhất context từ các nhánh thành một context đi tiếp.

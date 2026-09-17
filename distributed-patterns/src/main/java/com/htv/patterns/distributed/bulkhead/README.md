# Bulkhead (distributed)

Cô lập tài nguyên (pool kết nối/thread) cho từng dịch vụ phụ thuộc,
để một dependency chậm không làm cạn kiệt toàn hệ thống.

## Khi nào dùng

- Nhiều dependency chia sẻ tài nguyên chung; cần "khoang kín" từng cái.

## Cấu trúc

```puml
classDiagram
    class Bulkhead {
        +execute(operation) Result
        +availableSlots() int
    }
```

## Lưu ý triển khai

- Giới hạn số call đồng thời; vượt hạn thì từ chối nhanh.
- Xem bản đầy đủ ở `resilience-patterns/.../bulkhead`.

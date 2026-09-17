# Bulkhead

Giới hạn số lần thực thi **đồng thời** bằng semaphore, cô lập tài
nguyên: một dependency chậm không thể chiếm hết thread của cả hệ
thống. Không xin được permit ngay thì từ chối nhanh.

## Khi nào dùng

- Nhiều dependency chia sẻ chung pool thread — cần "khoang kín" cho từng cái.
- Chặn hiện tượng cạn kiệt thread (thread starvation) khi một call bị chậm.

## Cấu trúc

```puml
classDiagram
    class SemaphoreBulkhead {
        -int maxConcurrentCalls
        -Semaphore semaphore
        +availableSlots() int
        +execute(ResilientOperation) Result
    }
```

```puml
sequenceDiagram
    participant Client
    participant BH as SemaphoreBulkhead

    Client->>BH: execute(op)
    alt còn slot
        BH->>BH: semaphore.tryAcquire()
        BH-->>Client: Result.success(...)
        note over BH: finally → semaphore.release()
    else đầy
        BH-->>Client: Result.failure("bulkhead full")
    end
```

## Lưu ý triển khai

- `tryAcquire()` **không chặn** → từ chối ngay thay vì xếp hàng vô hạn.
- Permit luôn được `release()` trong `finally`, kể cả khi thao tác lỗi.

# Timeout / Time Limiter (distributed)

Giới hạn thời gian chờ một lời gọi từ xa, bỏ dở khi vượt hạn để
không giữ tài nguyên vô hạn.

## Khi nào dùng

- Call mạng có nguy cơ treo/chậm bất định.
- Cần chặn thời gian cho từng lần gọi (kể cả khi retry).

## Cấu trúc

```puml
classDiagram
    class TimeLimiter {
        +call(operation, duration) Result
    }
```

## Lưu ý triển khai

- Quá hạn trả `Result.failure`; thao tác cần tôn trọng interrupt để dừng thật.
- Xem bản đầy đủ ở `resilience-patterns/.../timeout`.

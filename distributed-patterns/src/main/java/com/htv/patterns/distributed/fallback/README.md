# Fallback

Cung cấp kết quả dự phòng khi lời gọi chính thất bại, giữ hệ thống
suy giảm mượt (graceful degradation) thay vì lỗi cứng.

## Khi nào dùng

- Có phương án thay thế chấp nhận được (cache cũ, giá trị mặc định).
- Muốn che lỗi tạm thời của dependency khỏi người dùng cuối.

## Cấu trúc

```puml
classDiagram
    class Fallback {
        +execute(primary, fallback) Result
    }
```

```puml
sequenceDiagram
    participant Client
    participant F as Fallback
    participant Primary
    participant Backup

    Client->>F: execute(primary, fallback)
    F->>Primary: run()
    Primary-->>F: lỗi
    F->>Backup: fallback()
    Backup-->>F: giá trị dự phòng
    F-->>Client: Result.success(dự phòng)
```

## Lưu ý triển khai

- Chỉ dùng fallback khi primary lỗi; giá trị dự phòng phải hợp lệ nghiệp vụ.
- Thường xếp ngoài cùng, sau Circuit Breaker/Retry.

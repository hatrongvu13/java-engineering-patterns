# Sharding

Phân mảnh dữ liệu/tải qua nhiều shard bằng băm khóa (hash), để mở
rộng theo chiều ngang.

## Khi nào dùng

- Dữ liệu quá lớn cho một node; cần chia đều theo khóa.
- Cần định tuyến ổn định: cùng khóa luôn về cùng shard.

## Cấu trúc

```puml
classDiagram
    class HashShardRouter {
        +shardFor(key) int
        +shardCount() int
    }
```

```puml
sequenceDiagram
    participant Client
    participant R as HashShardRouter

    Client->>R: shardFor("user-42")
    R->>R: hash(key) % shardCount
    R-->>Client: shard #k
```

## Lưu ý triển khai

- Ánh xạ tất định `hash(key) % shardCount` → cùng khóa luôn cùng shard.
- Đổi số shard sẽ tái phân bố khóa (production cân nhắc consistent hashing).

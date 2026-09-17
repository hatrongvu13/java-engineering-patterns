# Distributed Lock

Khoá loại trừ tương hỗ (mutual exclusion) chia sẻ giữa nhiều node,
để chỉ một node giữ tài nguyên tại một thời điểm.

## Khi nào dùng

- Nhiều instance cùng cần truy cập độc quyền một tài nguyên dùng chung.
- Cần khoá có thời hạn (lease) tránh deadlock khi node giữ khoá chết.

## Cấu trúc

```puml
classDiagram
    class InMemoryDistributedLock {
        +tryAcquire(key, owner, ttl) boolean
        +release(key, owner) boolean
    }
```

```puml
sequenceDiagram
    participant A as Node A
    participant L as DistributedLock
    participant B as Node B

    A->>L: tryAcquire(key, A, ttl)
    L-->>A: true (giữ khoá)
    B->>L: tryAcquire(key, B, ttl)
    L-->>B: false (đang bị giữ)
    A->>L: release(key, A)
```

## Lưu ý triển khai

- Khoá gắn `owner` + TTL; chỉ chủ sở hữu mới release được.
- Bản in-memory minh hoạ ngữ nghĩa; production dùng Redis/ZooKeeper/etcd.

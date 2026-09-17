# Leader Election

Chọn một node làm leader trong cụm để điều phối công việc không nên
chạy đồng thời ở nhiều node. Hiện thực theo thuật toán Bully.

## Khi nào dùng

- Chỉ một node được thực thi một tác vụ (scheduler, cleanup).
- Cần bầu lại leader tự động khi leader hiện tại chết.

## Cấu trúc

```puml
classDiagram
    class BullyLeaderElection {
        +elect(nodeId) Result
        +currentLeader() Optional
    }
```

```puml
sequenceDiagram
    participant N2 as Node 2
    participant N3 as Node 3 (id cao hơn)

    N2->>N3: election (phát hiện thiếu leader)
    N3-->>N2: id tôi cao hơn → tôi làm leader
    N3->>N3: trở thành leader
```

## Lưu ý triển khai

- Bully: node có id lớn nhất còn sống thắng cử.
- `currentLeader()` trả `Optional` khi chưa có leader.

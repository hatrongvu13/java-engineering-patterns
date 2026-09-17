# Load Balancer

Phân phối yêu cầu qua nhiều instance dịch vụ. Hiện thực chiến lược
Round-Robin.

## Khi nào dùng

- Có nhiều instance tương đương, cần rải tải đều.
- Kết hợp Service Discovery để lấy danh sách instance.

## Cấu trúc

```puml
classDiagram
    class RoundRobinLoadBalancer {
        +choose(List~ServiceInstance~) Optional~ServiceInstance~
    }
    class ServiceInstance
    RoundRobinLoadBalancer ..> ServiceInstance : selects
```

```puml
sequenceDiagram
    participant Client
    participant LB as RoundRobinLoadBalancer

    Client->>LB: choose([i1,i2,i3])
    LB-->>Client: i1
    Client->>LB: choose([i1,i2,i3])
    LB-->>Client: i2
    Client->>LB: choose([i1,i2,i3])
    LB-->>Client: i3
```

## Lưu ý triển khai

- Con trỏ vòng tăng dần theo mỗi lần chọn (an toàn đồng thời).
- Danh sách rỗng → trả `Optional.empty()`.

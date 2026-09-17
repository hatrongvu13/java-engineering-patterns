# Service Discovery

Tra cứu một instance dịch vụ khả dụng theo tên, dựa trên Service
Registry, để client gọi dịch vụ mà không cần biết địa chỉ cố định.

## Khi nào dùng

- Client cần tìm "một instance của service X" tại runtime.
- Kết hợp với Load Balancer để chọn instance cụ thể.

## Cấu trúc

```puml
classDiagram
    class ServiceDiscovery {
        +discover(serviceName) Optional~ServiceInstance~
    }
    class ServiceRegistry {
        <<interface>>
    }
    ServiceDiscovery --> ServiceRegistry : queries
```

```puml
sequenceDiagram
    participant Client
    participant SD as ServiceDiscovery
    participant Reg as ServiceRegistry

    Client->>SD: discover("payment")
    SD->>Reg: instances("payment")
    Reg-->>SD: [i1, i2]
    SD-->>Client: một instance khả dụng
```

## Lưu ý triển khai

- Trả `Optional`/`Result` khi không có instance nào.
- Việc chọn instance nào thuộc về Load Balancer.

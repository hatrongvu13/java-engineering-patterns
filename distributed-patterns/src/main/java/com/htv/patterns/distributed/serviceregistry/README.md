# Service Registry

Sổ đăng ký các instance dịch vụ đang sống, để các thành phần khác
tra cứu địa chỉ động thay vì hardcode.

## Khi nào dùng

- Instance dịch vụ thay đổi động (scale, restart, dời host).
- Là nền cho Service Discovery và Load Balancer.

## Cấu trúc

```puml
classDiagram
    class ServiceRegistry {
        <<interface>>
        +register(ServiceInstance) void
        +deregister(id) void
        +instances(serviceName) List
    }
    class InMemoryServiceRegistry
    class ServiceInstance {
        +id()
        +serviceName()
        +host()
        +port()
    }
    ServiceRegistry <|.. InMemoryServiceRegistry
    InMemoryServiceRegistry o-- ServiceInstance
```

## Lưu ý triển khai

- `ServiceInstance` (ở package `common`) là record bất biến mô tả một endpoint.
- `InMemoryServiceRegistry` giữ map serviceName → danh sách instance; production dùng Consul/Eureka.

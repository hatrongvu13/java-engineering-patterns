# Health Check

Kiểm tra tình trạng một dịch vụ/instance để hệ thống định tuyến
tránh instance hỏng và hỗ trợ tự phục hồi.

## Khi nào dùng

- Load balancer / discovery cần loại instance không khỏe.
- Cần probe định kỳ (liveness/readiness).

## Cấu trúc

```puml
classDiagram
    class HealthCheck {
        +check(ServiceInstance) boolean
    }
```

## Lưu ý triển khai

- Trả trạng thái khỏe/không khỏe cho một instance.
- Kết hợp Service Registry để loại instance hỏng khỏi danh sách khả dụng.

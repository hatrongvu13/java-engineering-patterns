# Hướng dẫn chọn pattern

Chọn pattern theo vấn đề bạn đang gặp.

## Tạo / khởi tạo đối tượng (creational)

| Vấn đề | Pattern |
|---|---|
| Cần đúng một thể hiện dùng chung | Singleton |
| Tạo đối tượng theo loại tại runtime | Factory (simple / method / abstract) |
| Dựng đối tượng nhiều tham số, bất biến | Builder (fluent / step / validated) |
| Nhân bản đối tượng có sẵn | Prototype |

## Cấu trúc / kết hợp đối tượng (structural)

| Vấn đề | Pattern |
|---|---|
| Ghép API không tương thích về một cổng chung | Adapter |
| Thêm hành vi (log, retry) mà không sửa lớp gốc | Decorator |
| Kiểm soát truy cập / cache trước một service đắt | Proxy |
| Ẩn nhiều subsystem sau một API đơn giản | Facade |
| Cây phần tử, xử lý leaf và group đồng nhất | Composite |
| Tách abstraction khỏi implementation | Bridge |
| Chia sẻ state nội tại giữa nhiều đối tượng | Flyweight |

## Hành vi / tương tác (behavioral)

| Vấn đề | Pattern |
|---|---|
| Đổi thuật toán tại runtime | Strategy |
| Chuỗi xử lý qua nhiều handler | Chain of Responsibility |
| Khung xử lý cố định, cho phép tùy biến bước | Template Method |
| Thông báo nhiều bên khi có sự kiện | Observer |

## Chịu lỗi / ổn định (resilience)

| Vấn đề | Pattern |
|---|---|
| Lỗi thoáng qua, thử lại được | Retry |
| Dependency chết kéo dài, cần ngắt nhanh | Circuit Breaker |
| Call chậm/treo, cần chặn thời gian | Timeout |
| Chống quá tải / giới hạn QPS | Rate Limiter |
| Cô lập tài nguyên, chặn cạn kiệt thread | Bulkhead |

Thứ tự kết hợp resilience khuyến nghị:
`Bulkhead → RateLimiter → CircuitBreaker → Retry → Timeout → operation`.

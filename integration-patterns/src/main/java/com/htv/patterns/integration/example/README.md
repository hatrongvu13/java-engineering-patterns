# Integration Example

Demo chạy được ghép các Enterprise Integration Patterns thành một
luồng xử lý đơn hàng đầu-cuối.

## Nội dung

`OrderIntegrationDemo` minh hoạ cách nối các pattern trong module:
kênh (`channel`) → định tuyến/lọc/tách-gộp (`routing`) → biến đổi
(`transformation`) → điểm cuối (`endpoint`), trên nền `core.Message`.

## Cách chạy

```bash
./mvnw -pl integration-patterns compile exec:java \
  -Dexec.mainClass=com.htv.patterns.integration.example.OrderIntegrationDemo
```

(hoặc chạy `main` của `OrderIntegrationDemo` trong IDE).

## Lưu ý

Đây là ví dụ minh hoạ luồng ghép pattern, không phải một pattern
riêng — đọc README của từng package `channel`/`routing`/
`transformation`/`endpoint` để hiểu từng thành phần.

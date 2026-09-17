# Pattern Catalog

## Retry
Thử lại lỗi tạm thời với số lần hữu hạn. Chỉ retry operation an toàn hoặc idempotent.

## Circuit Breaker
`CLOSED` cho phép gọi, `OPEN` fail-fast, `HALF_OPEN` thăm dò phục hồi.

## Timeout
Giới hạn thời gian chờ và hủy tác vụ khi vượt ngân sách.

## Fallback
Trả kết quả thay thế có chủ đích khi dependency lỗi.

## Bulkhead
Giới hạn tài nguyên đồng thời để cô lập lỗi và tránh lan truyền.

## Service Registry, Discovery, Load Balancer
Instance đăng ký vào registry; client discovery tìm instance; round-robin phân phối lần lượt.

## Saga
Chuỗi local transaction. Khi một bước lỗi, các bước đã thành công được compensate theo thứ tự ngược.

## Transactional Outbox
Lưu business state và event trong cùng local transaction; publisher gửi event sau. Bản in-memory minh họa hàng đợi pending.

## Idempotent Consumer
Theo dõi message ID để không áp dụng cùng một sự kiện nhiều lần.

## Leader Election
Chọn một node điều phối. Ví dụ Bully chọn active node có ID lớn nhất.

## Distributed Lock
Lease có owner và TTL. Production cần atomic store và fencing token để tránh stale owner.

## Sharding
Ánh xạ key ổn định sang shard. Khi thay đổi số shard, nên cân nhắc consistent hashing.

## Health Check
Chuẩn hóa trạng thái `UP`, `DOWN`, `DEGRADED` cho monitoring/orchestration.

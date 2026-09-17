# Onboarding cho lập trình viên mới

Chào mừng bạn. Tài liệu này giúp bạn đóng góp đúng quy ước trong
vòng vài phút. Đọc kèm `docs/architecture.md` và `docs/development.md`.

## 1. Chạy thử dự án

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
MVN='/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn'
"$MVN" test
```

Thấy `BUILD SUCCESS` là môi trường đã sẵn sàng.

## 2. Bố cục & nơi đặt code

Package theo: `com.htv.patterns.<category>.<pattern>.<role>`
(xem `.kiro/steering/structure.md`). Thêm pattern mới → tạo package
`<pattern>` trong module category tương ứng, kèm README + test.

## 3. Quy ước đặt tên

| Đối tượng | Quy ước | Ví dụ |
|---|---|---|
| Class | `PascalCase`, danh từ | `CircuitBreaker`, `StripeGatewayAdapter` |
| Interface (port) | danh từ, không tiền tố `I` | `PaymentGateway`, `MessageSender` |
| Value object | `record`, validate trong compact constructor | `ChargeRequest`, `RetryPolicy` |
| Factory tĩnh | `Default…` + `create()` | `DefaultNotificationStrategyRegistry` |
| Exception | tên theo lỗi, hậu tố `Exception` | `StrategyNotFoundException`, `ResilienceException` |
| Test class / method | `<Type>Test` / `should…` | `RetryExecutorTest#shouldSucceedAfterTransientFailures` |

## 4. Quy tắc code (bắt buộc)

- API public trả `Result<T>` (từ `pattern-core`), **không** ném exception qua ranh giới.
- `Objects.requireNonNull(x, "x must not be null")` cho mọi tham số public.
- Class `final` trừ khi thiết kế để kế thừa; class tiện ích có constructor private ném `AssertionError`.
- Code phụ thuộc thời gian nhận `java.time.Clock` inject được — **không** gọi `Instant.now()` trực tiếp.
- Mỗi pattern có `README.md` với sơ đồ PlantUML (class + sequence); mẫu: `structural-patterns/.../adapter/README.md`.

## 5. Viết test

- JUnit 5 + AssertJ. Mỗi pattern cần ít nhất một ca thành công và một ca thất bại.
- Test mirror cây package của main dưới `src/test/java`.
- Dùng `Clock`/`Sleeper` giả để test tất định, không `Thread.sleep` thật.

## 6. Quy trình Git

- Nhánh `main` được bảo vệ — không push trực tiếp.
- Nhánh làm việc: `feat/<mô-tả>`, `fix/<mô-tả>`, `docs/<mô-tả>`.
- Commit ngắn gọn, thì hiện tại: `add retry pattern`, `fix creational README`.
- Trước khi mở PR: `"$MVN" clean verify` phải xanh.
- PR mô tả: thay đổi gì, đã test gì, ảnh hưởng module nào.

## 7. Định nghĩa "hoàn thành" một pattern

1. Code trong package `<pattern>` đúng quy ước.
2. Test JUnit 5 xanh (có ca lỗi).
3. `README.md` của pattern với sơ đồ PlantUML.
4. Cập nhật README module (bảng catalog) nếu cần.
5. `"$MVN" -pl <module> test` → `BUILD SUCCESS`.

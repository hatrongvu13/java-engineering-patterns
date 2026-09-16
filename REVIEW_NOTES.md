# REVIEW NOTES — java-engineering-patterns

Ngày: 2026-09-17
Người thực hiện: Kiro (theo yêu cầu "hoàn thiện + tối ưu + ghi chú review")
Build: toàn bộ reactor `mvn test` → **BUILD SUCCESS** (Java 17, Maven 3.9.11).

Đây là bản ghi các thay đổi ĐÃ ÁP DỤNG trong lượt này, cùng các
ĐỀ XUẤT tối ưu code cũ (chưa áp dụng — chờ bạn duyệt để tránh phá vỡ
API/test đang xanh).

---

## 1. ĐÃ HOÀN THIỆN — pattern-core

Trước đây `Result`, `ExecutionContext`, `ExecutionReport` là class rỗng.

| File | Nội dung mới |
|---|---|
| `result/Result.java` | `sealed interface` + `Success`/`Failure` record. API: `success/failure/of/map/flatMap/onSuccess/onFailure/orElse/orElseGet/toOptional`. Không ném exception qua ranh giới. |
| `execution/ExecutionContext.java` | Bất biến, có `Builder`, `Clock` injectable, `attributes` immutable (`Map.copyOf`). |
| `execution/ExecutionReport.java` | `record`: context + `PatternStatus` + `Duration` + detail; `from(context, result, elapsed)`. |
| `metadata/PatternStatus.java` | Sửa lỗi chính tả `PLANED` → `PLANNED` (chưa nơi nào dùng nên an toàn). |
| `pattern-core/pom.xml` | Thay JUnit 3.8.1 → JUnit5 + AssertJ; thêm `<release>17</release>`. |

Test mới: `ResultTest` (12), `ExecutionContextTest` (5), `ExecutionReportTest` (4) — 21/21 PASS.

---

## 2. ĐÃ HOÀN THIỆN — structural-patterns (7/7 GoF)

Miền nghiệp vụ chung: thanh toán / thông báo, để các pattern ghép được với nhau.
Xoá 2 stub `App.java` / `AppTest.java`.

| Pattern | Package | Kịch bản | Test |
|---|---|---|---|
| Adapter | `adapter` | Hợp nhất SDK Stripe (cents) + PayPal (string) về `PaymentGateway` | 3 |
| Decorator | `decorator` | Logging + Retry bọc `PaymentGateway`, có thể xếp chồng | 3 |
| Proxy | `proxy` | Cache `ExchangeRateService` tốn kém | 3 |
| Facade | `facade` | `CheckoutFacade.checkout()` điều phối rate + gateway | 2 |
| Composite | `composite` | Cây hoá đơn `LineItem` + `InvoiceGroup`, tổng đệ quy | 3 |
| Bridge | `bridge` | Tách loại thông báo khỏi kênh gửi | 2 |
| Flyweight | `flyweight` | Chia sẻ `CurrencyStyle` qua factory pool | 3 |

- `structural-patterns/pom.xml`: JUnit5 + AssertJ + Mockito, `<release>17>`, phụ thuộc `pattern-core`.
- README module + 7 README con (PlantUML class + sequence, đúng form Strategy).
- Kết quả struct dùng `Result<ChargeReceipt>` từ pattern-core → chứng minh core hoạt động liên module.

---

## 3. ĐÃ SỬA — tài liệu & cấu hình lỗi thời

| File | Thay đổi |
|---|---|
| `creational-patterns/README.md` | Singleton "In progress" → **Completed** (đủ 8 biến thể đã có code); Factory/Builder/Prototype "Planned" → **Completed**; sửa path sai `io.github.hatrongvu` → `com.htv`. |
| `behavioral-patterns/README.md` | File rỗng → catalog Strategy/Chain/Template/Observer. |
| `pom.xml` (cha), `creational`, `behavioral` | `<source>16>/<target>16>` → `<release>17>`. Lý do: enforcer yêu cầu Java `[17,21)` nhưng compiler đặt 16 → mâu thuẫn; records/sealed cần 17. |

---

## 4. ĐỀ XUẤT — tối ưu code cũ (CHƯA áp dụng, chờ duyệt)

Những thay đổi dưới đây làm code nhất quán hơn với `pattern-core`,
nhưng ĐỔI CONTRACT nên sẽ ảnh hưởng test hiện có — cần bạn đồng ý:

1. **`behavioral…strategy.registry.NotificationService.createMessage`**
   hiện ném `RuntimeException` khi resolve/execute lỗi.
   → Đề xuất: trả `Result<NotificationMessage>` để đồng bộ với structural.
   Ảnh hưởng: `NotificationServiceTest` phải sửa theo.

2. **`creational…factory.notification.NotificationResult`** là một
   record kết quả riêng. → Có thể để nguyên (nó là value object, không
   phải result functional) — KHÔNG khuyến nghị đổi sang `Result`.

3. **`ExecutionContext`/`ExecutionReport` chưa được pattern nào dùng.**
   → Đề xuất (tuỳ chọn): thêm một `PatternRunner` mỏng trong
   pattern-core bọc mọi `execute()` để sinh `ExecutionReport` (timing +
   status) tự động — hữu ích cho module `benchmarks`.

4. **Các module còn trống** (integration, resilience, distributed,
   workflow, case-studies, benchmarks) vẫn là stub `App.java` +
   JUnit 3.8.1. → Lượt sau: nâng pom + triển khai theo cùng form.

5. **Hạ tầng**: chưa có `.github/workflows/*.yml` (CI), chưa có ngưỡng
   JaCoCo `<check>`, chưa có formatter (Spotless/Checkstyle). Ảnh PNG
   ~300KB nằm lẫn trong `creational-patterns/` nên chuyển vào
   `docs/diagrams/`.

---

## 5. Cách build/kiểm tra lại

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
MVN='/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn'
"$MVN" -o test            # toàn bộ
"$MVN" -o -pl structural-patterns test
```

Lưu ý: máy chưa có `mvn` trên PATH (Homebrew maven là symlink hỏng);
đang dùng Maven bundled của IntelliJ. Nên `brew reinstall maven` hoặc
thêm Maven Wrapper (`mvn wrapper:wrapper`) để CI/đồng đội dùng nhất quán.

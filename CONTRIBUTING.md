# Đóng góp

Cảm ơn bạn đã đóng góp cho `java-engineering-patterns`.

## Trước khi bắt đầu

- Đọc `docs/onboarding.md` (quy ước) và `docs/development.md` (build/test).
- Định hướng nền tảng cho Kiro: `.kiro/steering/`.

## Quy trình

1. Tạo nhánh từ `main`: `feat/…`, `fix/…`, hoặc `docs/…`.
2. Code theo quy ước (xem dưới) và viết test.
3. Chạy `"$MVN" clean verify` — phải `BUILD SUCCESS`.
4. Mở PR: mô tả thay đổi, đã test gì, ảnh hưởng module nào.
5. Không push trực tiếp vào `main` (nhánh được bảo vệ).

## Quy ước code (tóm tắt)

- API public trả `Result<T>`, không ném exception qua ranh giới.
- Value object là `record`, validate trong compact constructor.
- `Objects.requireNonNull` cho mọi tham số public.
- Code phụ thuộc thời gian nhận `Clock` inject được.
- Mỗi pattern: package `com.htv.patterns.<category>.<pattern>.<role>`, kèm `README.md` (PlantUML class + sequence) và test JUnit 5 + AssertJ (`should…`, có ca lỗi).

## Định nghĩa hoàn thành

Một pattern được coi là xong khi: code đúng quy ước + test xanh (có ca
lỗi) + README pattern + cập nhật catalog README module + `-pl <module>
test` báo `BUILD SUCCESS`.

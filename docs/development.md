# Hướng dẫn phát triển

## Yêu cầu môi trường

| Công cụ | Phiên bản | Ghi chú |
|---|---|---|
| JDK | **17** (bắt buộc `[17, 21)`) | Maven Enforcer sẽ chặn nếu sai |
| Maven | **3.9+** | Xem lưu ý bên dưới |

Kiểm tra:

```bash
java -version          # phải là 17.x
/usr/libexec/java_home -v 17   # in đường dẫn JDK 17 trên macOS
```

## Lưu ý về Maven trên máy hiện tại

Máy này **chưa có `mvn` trên PATH** (Homebrew maven là symlink hỏng)
và repo **chưa có Maven Wrapper**. Cách chạy hiện dùng Maven bundled
của IntelliJ:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
MVN='/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn'
```

Khuyến nghị khắc phục lâu dài (chọn 1):

```bash
brew reinstall maven                 # sửa symlink hỏng
# hoặc thêm wrapper để CI/đồng đội dùng nhất quán:
"$MVN" -N wrapper:wrapper            # tạo ./mvnw
```

## Cài dependency & build

Dependency được Maven tải tự động về `~/.m2`. Vì là multi-module,
`pattern-core` cần được cài vào local repo trước khi các module khác
resolve nó khi build lẻ:

```bash
"$MVN" -pl pattern-core install -DskipTests   # publish core
```

Build/test toàn dự án:

```bash
"$MVN" test          # biên dịch + chạy test tất cả module
"$MVN" verify        # test + JaCoCo coverage report
"$MVN" clean install # build sạch + cài vào ~/.m2
```

## Chạy / test một module hoặc một test

```bash
"$MVN" -pl resilience-patterns test          # một module
"$MVN" -pl structural-patterns test -Dtest=CheckoutFacadeTest   # một test class
```

Báo cáo coverage sau `verify`: `target/site/jacoco/index.html`
trong mỗi module.

## Cấu trúc build

- Cấu hình chung (phiên bản plugin, JUnit BOM, AssertJ, Mockito, POI, JaCoCo, Enforcer) nằm ở `pom.xml` cha.
- Module con khai báo dependency **không kèm version** (kế thừa từ `dependencyManagement`).
- Compiler đặt `<release>17</release>` để dùng `record`/`sealed`.

## Trước khi mở PR

```bash
"$MVN" clean verify   # phải BUILD SUCCESS, test xanh, coverage sinh ra
```

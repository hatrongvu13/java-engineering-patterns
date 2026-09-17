# Changelog

Định dạng theo [Keep a Changelog](https://keepachangelog.com/);
dự án dùng [Semantic Versioning](https://semver.org/).

## [Unreleased]

### Added
- `pattern-core`: `Result<T>` (sealed functional result), `ExecutionContext` (builder bất biến, Clock inject), `ExecutionReport` + test.
- `structural-patterns`: Adapter, Decorator, Proxy, Facade, Composite, Bridge, Flyweight — kèm test và README từng pattern.
- `resilience-patterns`: Retry, Circuit Breaker, Timeout, Rate Limiter, Bulkhead — kèm test và README từng pattern.
- `docs/`: `architecture.md`, `development.md`, `onboarding.md`, `pattern-selection-guide.md`, `module-overview.md`.
- `.kiro/steering/`: `product.md`, `tech.md`, `structure.md`.

### Changed
- Nâng compiler `source/target 16` → `<release>17>` ở `pom.xml` cha, `creational`, `behavioral`, và các module mới.
- Cập nhật `creational-patterns/README.md` đúng trạng thái thực tế; sửa path package sai `io.github.hatrongvu` → `com.htv`.
- Điền `behavioral-patterns/README.md` (trước đó rỗng).

### Fixed
- Sửa lỗi chính tả enum `PatternStatus.PLANED` → `PLANNED`.

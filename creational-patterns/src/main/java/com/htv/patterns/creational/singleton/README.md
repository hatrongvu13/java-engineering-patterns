# Java Singleton Catalog

So sánh nhiều cách hiện thực Singleton trong Java, từ ví dụ tối
giản đến biến thể thread-safe và cấu hình được.

## Mục tiêu học

- Hiểu cấu trúc Singleton cơ bản.
- So sánh khởi tạo eager và lazy.
- Tái hiện race condition ở lazy init ngây thơ.
- Hiểu chi phí đồng bộ hóa.
- Khám phá cơ chế khởi tạo do JVM hỗ trợ (Holder, Enum).
- Đánh giá vấn đề serialization và reflection.
- Hiện thực khởi tạo trì hoãn cấu hình được (supplier-backed).

## Tổng quan các biến thể

```puml
classDiagram
    class BasicSingleton
    class EagerSingleton
    class LazySingleton
    class SynchronizedSingleton
    class DoubleCheckedSingleton
    class HolderSingleton
    class EnumerationSingleton {
        <<enumeration>>
    }
    class SupplierBackSingleton~T~

    note for LazySingleton "Không thread-safe (minh họa race condition)"
    note for HolderSingleton "Lazy + thread-safe, không đồng bộ khi đọc"
    note for EnumerationSingleton "An toàn với serialization & reflection"
```

## Bảng so sánh

| Implementation | Lazy | Thread-safe | Đồng bộ mỗi lần đọc | Serializable mặc định | Cấu hình runtime |
|---|---:|---:|---:|---:|---:|
| Basic | No | Yes | No | No | No |
| Eager | No | Yes | No | No | No |
| Lazy | Yes | **No** | No | No | No |
| Synchronized | Yes | Yes | **Yes** | No | No |
| Double-checked | Yes | Yes | Chỉ lần tạo đầu | No | No |
| Holder | Yes | Yes | No | No | No |
| Enum | No | Yes | No | **Yes** | No |
| Supplier-backed | Yes | Yes | No | No | **Yes** |

## Supplier-backed Singleton — cấu trúc & luồng

```puml
classDiagram
    class SupplierBackSingleton~T~ {
        -Object monitor
        -Supplier~T~ supplier
        -T instance
        +configure(Supplier~T~) void
        +isConfigured() boolean
        +isInitialized() boolean
        +getInstance() T
    }

    class Supplier~T~ {
        <<interface>>
        +get() T
    }

    SupplierBackSingleton o-- Supplier
```

```puml
sequenceDiagram
    participant App
    participant Container as SupplierBackSingleton
    participant Supplier
    participant Service

    App->>Container: configure(supplier)
    Note over Container: instance vẫn null (lazy)
    App->>Container: getInstance()
    Container->>Container: instance == null → acquire monitor
    Container->>Supplier: get()
    Supplier->>Service: new Service()
    Service-->>Container: service
    Container->>Container: instance = service; supplier = null
    Container-->>App: service
    App->>Container: getInstance()
    Container-->>App: cùng một service
```

## Lưu ý quan trọng

Một số hiện thực trong catalog cố ý **chưa an toàn** (ví dụ Lazy) để
minh họa vấn đề mà biến thể sau giải quyết. Trước khi dùng cho
production, hãy rà: thread safety, serialization, reflection,
testability, vòng đời, và giải pháp Dependency Injection thay thế.

## Khuyến nghị chọn biến thể

- **Holder** — cần lazy, không tham số runtime, vòng đời theo ClassLoader, code gọn.
- **Enum** — cần định danh serialization, một thể hiện cố định, không kế thừa.
- **Supplier-backed** — chiến lược tạo cấu hình lúc runtime, khởi tạo trì hoãn, đối tượng đắt.
- **Dependency Injection** — app đã dùng Spring, đối tượng có phụ thuộc, cần quản lý vòng đời và thay thế khi test.

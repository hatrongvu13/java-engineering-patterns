# Message Transformation

Biến đổi nội dung/định dạng message giữa các hệ thống có mô hình dữ
liệu khác nhau.

## Khi nào dùng

- **MessageTranslator**: chuyển message từ định dạng A sang B.
- **ContentEnricher**: bổ sung dữ liệu còn thiếu (tra cứu thêm nguồn ngoài).
- **Normalizer**: đưa nhiều định dạng đầu vào khác nhau về một dạng chuẩn.

## Cấu trúc

```puml
classDiagram
    class MessageTranslator
    class ContentEnricher
    class Normalizer
    class Message~T~

    MessageTranslator ..> Message : in → out
    ContentEnricher ..> Message : thêm dữ liệu
    Normalizer ..> Message : nhiều dạng → 1 dạng
```

```puml
sequenceDiagram
    participant Src
    participant N as Normalizer
    participant E as ContentEnricher
    participant T as MessageTranslator
    participant Dst

    Src->>N: message (định dạng lạ)
    N->>E: message chuẩn hoá
    E->>T: message + dữ liệu bổ sung
    T-->>Dst: message định dạng đích
```

## Lưu ý triển khai

- Mỗi transformer nhận `Message` và trả `Message` mới (bất biến, không sửa tại chỗ).
- `ContentEnricher` có thể gọi nguồn ngoài để lấy dữ liệu bổ sung.

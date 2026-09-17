# Template Method

Cố định khung thuật toán sinh tài liệu trong lớp cha
(`AbstractDocumentGenerator`), để lớp con quyết định bước render
cụ thể (Email / Excel / Word).

## Khi nào dùng

- Nhiều biến thể chia sẻ **cùng khung xử lý**, chỉ khác vài bước.
- Muốn tránh lặp code khung và tập trung điểm mở rộng.

## Cấu trúc

```puml
classDiagram
    class AbstractDocumentGenerator {
        <<abstract>>
        +generate(DocumentRequest) DocumentResult
        #render(DocumentData)* RenderedDocument
        #format()* DocumentFormat
    }
    class EmailTemplateGenerator
    class ExcelReportGenerator
    class WordDocumentGenerator
    class DocumentStorage {
        <<interface>>
        +save(RenderedDocument) String
    }
    class InMemoryDocumentStorage

    AbstractDocumentGenerator <|-- EmailTemplateGenerator
    AbstractDocumentGenerator <|-- ExcelReportGenerator
    AbstractDocumentGenerator <|-- WordDocumentGenerator
    AbstractDocumentGenerator --> DocumentStorage
    DocumentStorage <|.. InMemoryDocumentStorage
```

```puml
sequenceDiagram
    participant Client
    participant Gen as AbstractDocumentGenerator
    participant Sub as WordDocumentGenerator
    participant Store as DocumentStorage

    Client->>Gen: generate(request)
    Gen->>Gen: validate + sanitize tên file
    Gen->>Sub: render(data)   %% bước trừu tượng
    Sub-->>Gen: RenderedDocument
    Gen->>Store: save(rendered)
    Store-->>Gen: documentId
    Gen-->>Client: DocumentResult
```

## Lưu ý triển khai

- `generate()` là template method `final` về mặt ý nghĩa: giữ cố định thứ tự validate → render → lưu.
- Bước `render()`/`format()` là hook trừu tượng cho lớp con.
- `FileNameSanitizer` chuẩn hoá tên file; lỗi bọc trong `DocumentGenerationException`.
- Excel/Word dùng Apache POI.

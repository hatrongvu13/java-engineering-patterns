```puml
classDiagram
    class OcrHandler {
        <<interface>>
        +code() String
        +setNext(OcrHandler) OcrHandler
        +handle(OcrProcessingContext) OcrProcessingContext
    }

    class AbstractOcrHandler {
        -OcrHandler next
        +setNext(OcrHandler) OcrHandler
        +handle(OcrProcessingContext) OcrProcessingContext
        #process(OcrProcessingContext) OcrProcessingContext
    }

    class FileValidationHandler
    class DimensionValidationHandler
    class ImageQualityHandler
    class OrientationHandler
    class NoiseReductionHandler
    class OcrReadinessHandler

    OcrHandler <|.. AbstractOcrHandler
    AbstractOcrHandler <|-- FileValidationHandler
    AbstractOcrHandler <|-- DimensionValidationHandler
    AbstractOcrHandler <|-- ImageQualityHandler
    AbstractOcrHandler <|-- OrientationHandler
    AbstractOcrHandler <|-- NoiseReductionHandler
    AbstractOcrHandler <|-- OcrReadinessHandler

    AbstractOcrHandler --> OcrHandler : next
    AbstractOcrHandler --> OcrProcessingContext
```

```puml
sequenceDiagram
    participant Client
    participant File as FileValidation
    participant Size as DimensionValidation
    participant Quality as ImageQuality
    participant Orientation
    participant Noise as NoiseReduction
    participant Ready as OcrReadiness

    Client->>File: handle(context)
    File->>Size: handle(context)
    Size->>Quality: handle(context)
    Quality->>Orientation: handle(context)
    Orientation->>Noise: handle(context)
    Noise->>Ready: handle(context)
    Ready->>Ready: markReadyForOcr()
    Ready-->>Client: processed context
```
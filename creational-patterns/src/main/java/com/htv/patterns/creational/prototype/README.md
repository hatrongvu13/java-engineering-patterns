```puml
classDiagram
    class Prototype~T~ {
        <<interface>>
        +copy() T
    }

    class ReportTemplatePrototype {
        -String code
        -String name
        -String language
        -List sections
        -Map parameters
        -ReportStyle style
        +copy() ReportTemplatePrototype
    }

    class ReportSection {
        -String code
        -String title
        -boolean enabled
    }

    class ReportStyle {
        -String fontName
        -String primaryColor
        -int fontSize
    }

    class ReportTemplateRegistry {
        -Map prototypes
        +register(String, ReportTemplatePrototype) void
        +replace(String, ReportTemplatePrototype) void
        +create(String) ReportTemplatePrototype
        +contains(String) boolean
        +remove(String) boolean
    }

    Prototype <|.. ReportTemplatePrototype
    ReportTemplatePrototype o-- ReportSection
    ReportTemplatePrototype o-- ReportStyle
    ReportTemplateRegistry o-- ReportTemplatePrototype
```

```puml
sequenceDiagram
    participant Bootstrap
    participant Registry as ReportTemplateRegistry
    participant Prototype as ReportTemplatePrototype
    participant Client

    Bootstrap->>Registry: register("monthly", prototype)
    Registry->>Prototype: copy()
    Prototype-->>Registry: stored defensive copy

    Client->>Registry: create("monthly")
    Registry->>Prototype: copy()
    Prototype-->>Registry: independent deep copy
    Registry-->>Client: report template

    Client->>Client: customize language and parameters

    Client->>Registry: create("monthly")
    Registry->>Prototype: copy()
    Prototype-->>Client: unchanged deep copy
```
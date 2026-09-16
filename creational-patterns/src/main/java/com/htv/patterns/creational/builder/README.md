```puml
classDiagram
    class ReportRequest {
        <<record>>
        +reportCode() String
        +format() ReportFormat
        +fromDate() LocalDate
        +toDate() LocalDate
        +language() String
        +includeSummary() boolean
        +includeDetails() boolean
        +includeChart() boolean
        +groupBy() List
        +parameters() Map
    }

    class ClassicBuilder {
        -String reportCode
        -ReportFormat format
        -LocalDate fromDate
        -LocalDate toDate
        +reportCode(String) ClassicBuilder
        +format(ReportFormat) ClassicBuilder
        +dateRange(LocalDate, LocalDate) ClassicBuilder
        +build() ReportRequest
    }

    class FluentBuilder {
        +asPdf() FluentBuilder
        +asExcel() FluentBuilder
        +between(LocalDate, LocalDate) FluentBuilder
        +withSummary() FluentBuilder
        +withDetails() FluentBuilder
        +build() ReportRequest
    }

    class StepBuilder {
        +reportCode(String) FormatStep
        +format(ReportFormat) DateRangeStep
        +dateRange(LocalDate, LocalDate) OptionalStep
        +build() ReportRequest
    }

    class ValidatedBuilder {
        +validate() List
        +build() ReportRequest
    }

    ClassicBuilder ..> ReportRequest : builds
    FluentBuilder ..> ReportRequest : builds
    StepBuilder ..> ReportRequest : builds
    ValidatedBuilder ..> ReportRequest : validates and builds
```

```puml
sequenceDiagram
    participant Client
    participant Builder as ReportRequest.Builder
    participant Validator as ReportRequestValidator
    participant Request as ReportRequest

    Client->>Builder: reportCode("monthly-finance")
    Client->>Builder: format(EXCEL)
    Client->>Builder: dateRange(from, to)
    Client->>Builder: includeSummary(true)
    Client->>Builder: groupBy("BRANCH")
    Client->>Builder: build()

    Builder->>Request: new ReportRequest(...)
    Request-->>Builder: immutable request

    Builder->>Validator: validate(request)
    Validator-->>Builder: valid

    Builder-->>Client: request
```

## Builder Pattern

Status: Completed

Implemented variants:

- Classic Builder
- Fluent Builder
- Step Builder
- Validated Builder

Domain example:

- Report request configuration
- Multiple output formats
- Date range
- Language
- Content selection
- Grouping fields
- Dynamic parameters

Key comparisons:

- Classic Builder provides flexible object construction.
- Fluent Builder expresses configuration using domain language.
- Step Builder enforces required construction stages at compile time.
- Validated Builder accumulates multiple validation errors.
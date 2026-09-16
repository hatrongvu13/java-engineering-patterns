# Behavioral Patterns

Patterns that describe how objects interact and distribute
responsibility. All patterns share a notification / document
domain.

## Catalog

| Pattern | Scenario | Status |
|---|---|---|
| Strategy | Channel-specific notification message building | Completed |
| Chain of Responsibility | OCR document validation pipeline | Completed |
| Template Method | Document generation (email / Excel / Word) | Completed |
| Observer | Domain-event publication for OCR processing | Completed |

## Conventions

- Value objects are `record`s validated in the compact constructor.
- Registries are built through a `Builder` and a `Default…Registry`
  factory.
- Tests use JUnit 5 + AssertJ, named `should…`, with failure cases.

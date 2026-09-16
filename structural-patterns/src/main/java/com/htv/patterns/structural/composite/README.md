# Composite

Treat a single invoice line and a whole group of lines uniformly
through one `InvoiceComponent` interface; totals fold recursively.

```puml
classDiagram
    class InvoiceComponent {
        <<interface>>
        +label() String
        +total() BigDecimal
        +lineCount() int
    }

    class LineItem
    class InvoiceGroup {
        -List children
        +add(InvoiceComponent) InvoiceGroup
    }

    InvoiceComponent <|.. LineItem
    InvoiceComponent <|.. InvoiceGroup
    InvoiceGroup o-- InvoiceComponent : children
```

```puml
sequenceDiagram
    participant Client
    participant Root as InvoiceGroup(order)
    participant Group as InvoiceGroup(hardware)
    participant Item as LineItem

    Client->>Root: total()
    Root->>Group: total()
    Group->>Item: total()
    Item-->>Group: 100.00
    Group-->>Root: 125.00
    Root-->>Client: 135.00
```

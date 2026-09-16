# Flyweight

Share one `CurrencyStyle` per currency code via a factory pool.
Intrinsic state (symbol, fraction digits) is shared; extrinsic
state (the amount) is passed at format time.

```puml
classDiagram
    class CurrencyStyle {
        -String symbol
        -int fractionDigits
        +format(BigDecimal) String
    }

    class CurrencyStyleFactory {
        -Map pool
        +styleFor(String) CurrencyStyle
    }

    CurrencyStyleFactory o-- CurrencyStyle : pools & shares
```

```puml
sequenceDiagram
    participant Client
    participant Factory as CurrencyStyleFactory
    participant Style as CurrencyStyle(USD)

    Client->>Factory: styleFor(USD)
    Factory->>Style: new (first time only)
    Factory-->>Client: CurrencyStyle(USD)
    Client->>Factory: styleFor(usd)
    Factory-->>Client: same CurrencyStyle(USD)
    Client->>Style: format(9.5)
    Style-->>Client: "$9.50"
```

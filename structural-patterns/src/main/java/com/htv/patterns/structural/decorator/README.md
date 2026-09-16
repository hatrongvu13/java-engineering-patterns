# Decorator

Attach cross-cutting behaviour (logging, retry) to a
`PaymentGateway` at runtime without touching the wrapped object.

```puml
classDiagram
    class PaymentGateway {
        <<interface>>
        +charge(ChargeRequest) Result~ChargeReceipt~
    }

    class PaymentGatewayDecorator {
        <<abstract>>
        #PaymentGateway delegate
    }

    class LoggingGatewayDecorator
    class RetryingGatewayDecorator

    PaymentGateway <|.. PaymentGatewayDecorator
    PaymentGatewayDecorator <|-- LoggingGatewayDecorator
    PaymentGatewayDecorator <|-- RetryingGatewayDecorator
    PaymentGatewayDecorator o-- PaymentGateway : wraps
```

```puml
sequenceDiagram
    participant Client
    participant Log as LoggingGatewayDecorator
    participant Retry as RetryingGatewayDecorator
    participant Real as PaymentGateway

    Client->>Log: charge(request)
    Log->>Retry: charge(request)
    Retry->>Real: charge(request)
    Real-->>Retry: Result (fail)
    Retry->>Real: charge(request) [retry]
    Real-->>Retry: Result (ok)
    Retry-->>Log: Result (ok)
    Log->>Log: record log line
    Log-->>Client: Result (ok)
```

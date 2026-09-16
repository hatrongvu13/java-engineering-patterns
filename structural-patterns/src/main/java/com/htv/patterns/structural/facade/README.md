# Facade

Expose one `checkout()` entry point over the currency-conversion
and payment-gateway subsystems.

```puml
classDiagram
    class CheckoutFacade {
        +checkout(CheckoutRequest) Result~ChargeReceipt~
    }

    class ExchangeRateService
    class PaymentGateway

    CheckoutFacade --> ExchangeRateService : converts
    CheckoutFacade --> PaymentGateway : charges
```

```puml
sequenceDiagram
    participant Client
    participant Facade as CheckoutFacade
    participant Rates as ExchangeRateService
    participant Gateway as PaymentGateway

    Client->>Facade: checkout(request)
    Facade->>Rates: rate(src, dst)
    Rates-->>Facade: rate
    Facade->>Facade: amount * rate
    Facade->>Gateway: charge(chargeRequest)
    Gateway-->>Facade: Result<ChargeReceipt>
    Facade-->>Client: Result<ChargeReceipt>
```

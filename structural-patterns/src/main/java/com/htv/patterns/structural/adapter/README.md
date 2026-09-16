# Adapter

Wrap incompatible third-party payment SDKs behind one unified
`PaymentGateway` port.

```puml
classDiagram
    class PaymentGateway {
        <<interface>>
        +provider() PaymentProvider
        +charge(ChargeRequest) Result~ChargeReceipt~
    }

    class StripeGatewayAdapter
    class PaypalGatewayAdapter
    class StripeClient
    class PaypalClient

    PaymentGateway <|.. StripeGatewayAdapter
    PaymentGateway <|.. PaypalGatewayAdapter
    StripeGatewayAdapter --> StripeClient : adapts
    PaypalGatewayAdapter --> PaypalClient : adapts
```

```puml
sequenceDiagram
    participant Client
    participant Adapter as StripeGatewayAdapter
    participant Sdk as StripeClient

    Client->>Adapter: charge(ChargeRequest)
    Adapter->>Adapter: amount -> cents
    Adapter->>Sdk: createPaymentIntent(cents, ccy, cust)
    Sdk-->>Adapter: StripeIntent
    Adapter->>Adapter: normalise -> ChargeReceipt
    Adapter-->>Client: Result<ChargeReceipt>
```

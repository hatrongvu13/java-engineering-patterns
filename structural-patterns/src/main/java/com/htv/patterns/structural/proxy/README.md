# Proxy

A caching proxy over an expensive `ExchangeRateService`: first
lookup hits the real service, later identical lookups are served
from cache.

```puml
classDiagram
    class ExchangeRateService {
        <<interface>>
        +rate(String, String) BigDecimal
    }

    class RemoteExchangeRateService
    class CachingExchangeRateProxy {
        -Map cache
    }

    ExchangeRateService <|.. RemoteExchangeRateService
    ExchangeRateService <|.. CachingExchangeRateProxy
    CachingExchangeRateProxy o-- ExchangeRateService : delegate
```

```puml
sequenceDiagram
    participant Client
    participant Proxy as CachingExchangeRateProxy
    participant Real as RemoteExchangeRateService

    Client->>Proxy: rate(USD, EUR)
    Proxy->>Real: rate(USD, EUR)  %% miss
    Real-->>Proxy: 0.92
    Proxy-->>Client: 0.92
    Client->>Proxy: rate(USD, EUR)
    Proxy-->>Client: 0.92  %% cache hit, no Real call
```

# Structural Patterns

Patterns that compose classes and objects into larger structures
while keeping those structures flexible and efficient. All
patterns share a payment / notification domain so they compose
with one another.

## Catalog

| Pattern | Scenario | Package |
|---|---|---|
| Adapter | Unify incompatible payment SDKs (Stripe cents, PayPal string) behind one `PaymentGateway` | `adapter` |
| Decorator | Add logging / retry to a `PaymentGateway` transparently | `decorator` |
| Proxy | Cache expensive `ExchangeRateService` lookups | `proxy` |
| Facade | One `CheckoutFacade.checkout()` over rate + gateway subsystems | `facade` |
| Composite | Recursive invoice tree (`LineItem` leaf + `InvoiceGroup`) | `composite` |
| Bridge | Decouple notification type from delivery channel | `bridge` |
| Flyweight | Share `CurrencyStyle` metadata via a factory pool | `flyweight` |

Status: **Completed** — every pattern has code, tests, and a
package README.

## Conventions

- Value objects are `record`s validated in the compact constructor.
- Public results use `com.htv.patterns.core.result.Result<T>` instead
  of throwing across boundaries.
- Tests use JUnit 5 + AssertJ, named `should…`, with failure cases.

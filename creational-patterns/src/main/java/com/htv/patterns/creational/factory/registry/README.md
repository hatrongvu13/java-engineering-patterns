```puml
classDiagram
    class NotificationSender {
        <<interface>>
        +supports() NotificationType
        +send(NotificationRequest) NotificationResult
    }

    class NotificationSenderRegistryBuilder {
        -Map factories
        +register(type, supplier) NotificationSenderRegistryBuilder
        +override(type, supplier) NotificationSenderRegistryBuilder
        +contains(type) boolean
        +registrationCount() int
        +build() NotificationSenderRegistry
    }

    class NotificationSenderRegistry {
        -Map factories
        +create(type) NotificationSender
        +contains(type) boolean
        +size() int
        +registeredTypes() Set
    }

    class DefaultNotificationRegistryFactory {
        +create() NotificationSenderRegistry
    }

    class RegistryNotificationService {
        -NotificationSenderRegistry registry
        +send(type, request) NotificationResult
    }

    NotificationSenderRegistryBuilder ..> NotificationSenderRegistry : builds
    NotificationSenderRegistry o-- NotificationSender
    DefaultNotificationRegistryFactory ..> NotificationSenderRegistryBuilder
    RegistryNotificationService --> NotificationSenderRegistry
```

## Registry-based Factory

Registry-based Factory replaces a centralized switch statement with
a mapping between product identifiers and creation functions.

### Components

- `NotificationType` is the product identifier.
- `Supplier<? extends NotificationSender>` creates a product.
- `NotificationSenderRegistryBuilder` configures registrations.
- `NotificationSenderRegistry` is the immutable runtime factory.
- `DefaultNotificationRegistryFactory` defines default registrations.
- `RegistryNotificationService` uses the registry through constructor
  injection.

### Lifecycle

1. Register product factories during application bootstrap.
2. Build an immutable registry snapshot.
3. Inject the registry into application services.
4. Create products through the registry.
5. Do not mutate registry configuration during request processing.

### Benefits

- Removes centralized switch statements.
- Supports extensible product registration.
- Keeps runtime configuration immutable.
- Simplifies testing through Supplier replacement.
- Separates bootstrap configuration from creation logic.

### Trade-offs

- Missing registrations are detected at runtime.
- Duplicate registrations require an explicit policy.
- A global registry can become a Service Locator.
- Registered Suppliers must return compatible products.
- Product lifecycle still needs an explicit design.

### Recommendation

Prefer constructor injection. Do not expose the registry as a global
static object.
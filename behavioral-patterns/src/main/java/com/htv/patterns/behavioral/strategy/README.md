```puml
classDiagram
    class NotificationStrategy {
        <<interface>>
        +supports() NotificationChannel
        +execute(NotificationRequest) NotificationMessage
    }

    class EmailNotificationStrategy
    class SmsNotificationStrategy
    class PushNotificationStrategy

    NotificationStrategy <|.. EmailNotificationStrategy
    NotificationStrategy <|.. SmsNotificationStrategy
    NotificationStrategy <|.. PushNotificationStrategy

    class NotificationContext {
        -NotificationStrategy strategy
        +setStrategy(NotificationStrategy) void
        +execute(NotificationRequest) NotificationMessage
    }

    class NotificationStrategyRegistryBuilder {
        -Map strategies
        +register(NotificationStrategy) NotificationStrategyRegistryBuilder
        +override(NotificationStrategy) NotificationStrategyRegistryBuilder
        +build() NotificationStrategyRegistry
    }

    class NotificationStrategyRegistry {
        -Map strategies
        +resolve(NotificationChannel) NotificationStrategy
        +contains(NotificationChannel) boolean
        +registeredChannels() Set
    }

    class NotificationService {
        -NotificationStrategyRegistry registry
        +createMessage(NotificationChannel, NotificationRequest) NotificationMessage
    }

    NotificationContext --> NotificationStrategy
    NotificationStrategyRegistry o-- NotificationStrategy
    NotificationStrategyRegistryBuilder ..> NotificationStrategyRegistry : builds
    NotificationService --> NotificationStrategyRegistry
```

```puml
sequenceDiagram
    participant Client
    participant Service as NotificationService
    participant Registry as NotificationStrategyRegistry
    participant Strategy as EmailNotificationStrategy

    Client->>Service: createMessage(EMAIL, request)
    Service->>Registry: resolve(EMAIL)
    Registry-->>Service: EmailNotificationStrategy
    Service->>Strategy: execute(request)
    Strategy-->>Service: NotificationMessage
    Service-->>Client: NotificationMessage
```
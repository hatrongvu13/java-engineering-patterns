```puml
classDiagram
    class NotificationSender {
        <<interface>>
        +supports() NotificationType
        +send(NotificationRequest) NotificationResult
    }

    class EmailNotificationSender
    class SmsNotificationSender
    class PushNotificationSender

    NotificationSender <|.. EmailNotificationSender
    NotificationSender <|.. SmsNotificationSender
    NotificationSender <|.. PushNotificationSender

    class SimpleNotificationFactory {
        +create(NotificationType) NotificationSender
    }

    SimpleNotificationFactory --> NotificationSender
    SimpleNotificationFactory ..> EmailNotificationSender : creates
    SimpleNotificationFactory ..> SmsNotificationSender : creates
    SimpleNotificationFactory ..> PushNotificationSender : creates
```

# Java Factory Catalog

This catalog demonstrates multiple object-creation approaches,
from a centralized Simple Factory to extensible registry-based
factories.

## Learning objectives

- Centralize object creation.
- Hide concrete implementations from clients.
- Compare Simple Factory and Static Factory Method.
- Understand the GoF Factory Method.
- Create related object families with Abstract Factory.
- Register implementations without modifying a switch statement.
- Integrate a factory registry with Spring dependency injection.

## Implementations

### Completed

- Simple Factory
- Static Factory Method

### Next

- Factory Method
- Abstract Factory
- Registry-based Factory
- Spring-managed Factory Registry
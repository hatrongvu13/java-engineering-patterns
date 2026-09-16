```puml

classDiagram
    class NotificationComponentFactory {
        <<interface>>
        +supports() NotificationType
        +createValidator() NotificationValidator
        +createFormatter() NotificationFormatter
        +createSender() NotificationSender
    }

    class NotificationValidator {
        <<interface>>
        +supports() NotificationType
        +validate(NotificationRequest) void
    }

    class NotificationFormatter {
        <<interface>>
        +supports() NotificationType
        +format(NotificationRequest) FormattedNotification
    }

    class NotificationSender {
        <<interface>>
        +supports() NotificationType
        +send(NotificationRequest) NotificationResult
    }

    class EmailNotificationComponentFactory
    class SmsNotificationComponentFactory
    class PushNotificationComponentFactory

    NotificationComponentFactory <|.. EmailNotificationComponentFactory
    NotificationComponentFactory <|.. SmsNotificationComponentFactory
    NotificationComponentFactory <|.. PushNotificationComponentFactory

    class EmailAddressValidator
    class HtmlEmailFormatter
    class EmailNotificationSender

    NotificationValidator <|.. EmailAddressValidator
    NotificationFormatter <|.. HtmlEmailFormatter
    NotificationSender <|.. EmailNotificationSender

    EmailNotificationComponentFactory ..> EmailAddressValidator : creates
    EmailNotificationComponentFactory ..> HtmlEmailFormatter : creates
    EmailNotificationComponentFactory ..> EmailNotificationSender : creates

    class NotificationProcessor {
        -NotificationValidator validator
        -NotificationFormatter formatter
        -NotificationSender sender
        +process(NotificationRequest) NotificationResult
    }

    NotificationProcessor --> NotificationComponentFactory
    NotificationProcessor --> NotificationValidator
    NotificationProcessor --> NotificationFormatter
    NotificationProcessor --> NotificationSender
```

```puml
sequenceDiagram
    participant Client
    participant Processor as NotificationProcessor
    participant Factory as EmailNotificationComponentFactory
    participant Validator as EmailAddressValidator
    participant Formatter as HtmlEmailFormatter
    participant Sender as EmailNotificationSender

    Client->>Processor: new NotificationProcessor(factory)

    Processor->>Factory: createValidator()
    Factory-->>Processor: EmailAddressValidator

    Processor->>Factory: createFormatter()
    Factory-->>Processor: HtmlEmailFormatter

    Processor->>Factory: createSender()
    Factory-->>Processor: EmailNotificationSender

    Client->>Processor: process(request)
    Processor->>Validator: validate(request)
    Processor->>Formatter: format(request)
    Formatter-->>Processor: formattedNotification
    Processor->>Sender: send(formattedRequest)
    Sender-->>Processor: result
    Processor-->>Client: result
```
# Bridge

Decouple the notification abstraction (alert, OTP) from the
delivery implementor (email, SMS) so each varies independently.

```puml
classDiagram
    class Notification {
        <<abstract>>
        #MessageSender sender
        +notify(String) String
    }

    class AlertNotification
    class OtpNotification

    class MessageSender {
        <<interface>>
        +send(String, String) String
    }

    class EmailMessageSender
    class SmsMessageSender

    Notification <|-- AlertNotification
    Notification <|-- OtpNotification
    MessageSender <|.. EmailMessageSender
    MessageSender <|.. SmsMessageSender
    Notification o-- MessageSender : bridge
```

```puml
sequenceDiagram
    participant Client
    participant Abs as AlertNotification
    participant Impl as SmsMessageSender

    Client->>Abs: notify(recipient)
    Abs->>Abs: render "[ALERT] ..."
    Abs->>Impl: send(recipient, payload)
    Impl-->>Abs: "sms://..."
    Abs-->>Client: "sms://..."
```

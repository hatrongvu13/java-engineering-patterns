# integration-patterns

Dự án ví dụ Enterprise Integration Patterns thuần Java 17, tập trung vào cách tổ chức package và ý tưởng cốt lõi, không phụ thuộc framework messaging.

## Package gốc

`com.htv.patterns.integration`

Không dùng `com.htv.patterns.structural.adapter.adapter` vì đó là namespace của nhóm Structural Design Patterns. Trong module này, mỗi nhóm Integration Pattern có package riêng: `core`, `channel`, `routing`, `transformation`, `endpoint`, `reliability`, `requestreply`, `example`.

## Pattern đã triển khai

- Channels: Point-to-Point Channel, Publish-Subscribe Channel
- Routing: Content-Based Router, Message Filter, Recipient List, Splitter, Aggregator
- Transformation: Message Translator, Content Enricher, Normalizer
- Endpoints: Idempotent Receiver, Competing Consumers
- Reliability: Retry, Dead Letter Channel
- Message construction/conversation: Correlation Identifier, Request-Reply

## Chạy dự án

Tại project cha, bảo đảm `pom.xml` có module:

```xml
<modules>
    <module>integration-patterns</module>
</modules>
```

Sau đó chạy:

```bash
mvn -pl integration-patterns -am clean test
mvn -pl integration-patterns -am package
```

Chạy demo từ IDE với class `com.htv.patterns.integration.example.OrderIntegrationDemo`.

## Lưu ý production

Các implementation là in-memory để minh họa pattern. Khi áp dụng thực tế, thay channel/store bằng Kafka, RabbitMQ, JMS hoặc database; bổ sung persistence, timeout, backoff, observability, graceful shutdown và xử lý race condition theo yêu cầu hệ thống.

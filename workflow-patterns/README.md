# Workflow Patterns

Module Java 17 minh họa workflow/control-flow patterns, giữ convention `com.htv.patterns.workflow.<pattern>.<pattern>` tương tự package cũ `com.htv.patterns.structural.proxy.proxy`.

## Pattern đã triển khai

- Sequence
- Parallel Split
- Synchronization
- Exclusive Choice
- Multi Choice
- Simple Merge
- Structured Loop
- Multiple Instances
- Cancellation Token
- Compensation
- Workflow State Machine

## Build

```bash
mvn clean test
mvn package
```

Trong parent POM thêm:

```xml
<module>workflow-patterns</module>
```

Sau đó chạy `mvn -pl workflow-patterns -am clean test`.

## Demo

Chạy `com.htv.patterns.workflow.example.example.LoanApprovalWorkflowDemo`.

Các implementation nhằm minh họa pattern. Với production cần persistence, durable timer, optimistic locking, idempotency, audit log, retry policy, distributed worker và versioning workflow.

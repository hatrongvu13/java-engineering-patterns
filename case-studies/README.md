# Case Studies

Mục tiêu của module là chứng minh nhiều pattern phối hợp thành một luồng nghiệp vụ, thay vì lặp lại ví dụ đơn lẻ.

## Kịch bản

1. **Order Fulfillment**: Saga, compensation, outbox, event-driven flow.
2. **Loan Approval**: sequence, parallel checks, exclusive decision.
3. **Resilient Client**: retry và fallback.
4. **Event Processing**: idempotent consumer.

## Quy ước

- Package: `com.htv.patterns.casestudies.<domain>`.
- Mỗi case study có happy path, failure path, timeline và test hành vi.
- Giai đoạn tiếp theo nên thay implementation nội bộ bằng dependency trực tiếp tới các module pattern sau khi API của chúng ổn định.

## Chạy

`mvn -pl case-studies -am clean verify`

# Benchmarks

Module dùng OpenJDK JMH để đo chi phí và trade-off của cách triển khai pattern. Không dùng unit test hoặc `System.nanoTime()` để kết luận performance.

## Benchmark hiện có

- Direct dispatch và Strategy dispatch.
- Constructor và Builder.
- If/else router và rule chain.
- Direct call và retry wrapper trên happy path.

## Chạy

```bash
mvn -pl benchmarks -am clean package -DskipTests
java -jar benchmarks/target/benchmarks.jar
java -jar benchmarks/target/benchmarks.jar RoutingBenchmark -prof gc
```

## Nguyên tắc review kết quả

- Không chạy benchmark trong build mặc định.
- Luôn giữ warmup, measurement và fork.
- Ghi CPU, OS, JDK, JVM flags và commit SHA.
- So sánh distribution/error thay vì chỉ nhìn một con số.
- Không đặt performance gate cứng từ laptop developer.

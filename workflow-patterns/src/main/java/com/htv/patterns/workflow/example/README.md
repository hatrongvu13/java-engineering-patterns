# Workflow Example

Demo chạy được ghép các workflow control-flow pattern thành một
luồng phê duyệt khoản vay đầu-cuối.

## Nội dung

`LoanApprovalWorkflowDemo` minh hoạ cách nối Sequence, Exclusive
Choice, Parallel Split + Synchronization, State Machine và
Compensation trên nền `core.WorkflowContext`.

## Cách chạy

```bash
./mvnw -pl workflow-patterns compile exec:java \
  -Dexec.mainClass=com.htv.patterns.workflow.example.LoanApprovalWorkflowDemo
```

(hoặc chạy `main` trong IDE).

## Lưu ý

Đây là ví dụ ghép pattern, không phải một pattern riêng — đọc README
từng package control-flow để hiểu từng khối.

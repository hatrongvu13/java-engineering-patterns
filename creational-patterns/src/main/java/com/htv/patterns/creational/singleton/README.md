# Java Singleton Catalog

This catalog compares multiple Singleton implementations in Java,
from minimal examples to thread-safe and configurable variants.

## Learning objectives

- Understand the basic Singleton structure.
- Compare eager and lazy initialization.
- Reproduce the race condition in naive lazy initialization.
- Understand synchronization costs.
- Explore JVM-supported initialization mechanisms.
- Evaluate serialization and reflection concerns.
- Implement configurable deferred initialization.

## Implementations

### Completed

- Basic Singleton
- Eager Singleton
- Lazy Singleton
- Synchronized Singleton

### Next

- Double-checked Locking Singleton
- Initialization-on-demand Holder
- Enum Singleton
- Supplier-backed Singleton

## Important note

Some implementations in this catalog are intentionally incomplete
or unsafe. They exist to demonstrate the problem solved by later
implementations.

Do not copy an implementation into production without reviewing:

- Thread safety
- Serialization
- Reflection
- Testability
- Lifecycle requirements
- Dependency injection alternatives

```shell

| Implementation | Lazy | Thread-safe | Synchronization per access | Serializable by default |
|---|---:|---:|---:|---:|
| Basic | No | Yes | No | No |
| Eager | No | Yes | No | No |
| Lazy | Yes | No | No | No |
| Synchronized | Yes | Yes | Yes | No |
| Double-checked | Yes | Yes | Initial creation path | No |
| Holder | Yes | Yes | No | No |
| Enum | No | Yes | No | Yes |
| Supplier-backed | Yes | Depends on implementation | Depends | No |
```


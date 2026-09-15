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
```puml
classDiagram
    class SupplierBackedSingleton~T~ {
        -Object monitor
        -Supplier~T~ supplier
        -T instance
        +configure(Supplier~T~) void
        +isConfigured() boolean
        +isInitialized() boolean
        +getInstance() T
        ~resetForTest() void
    }

    class Supplier~T~ {
        <<interface>>
        +get() T
    }

    class TestService {
        +name() String
    }

    SupplierBackedSingleton o-- Supplier
    SupplierBackedSingleton --> TestService
```

```puml
sequenceDiagram
    participant App
    participant Container as SupplierBackedSingleton
    participant Supplier
    participant Service

    App->>Container: configure(supplier)
    Note over Container: instance remains null

    App->>Container: getInstance()
    Container->>Container: instance == null
    Container->>Container: acquire monitor
    Container->>Supplier: get()
    Supplier->>Service: new Service()
    Service-->>Supplier: service
    Supplier-->>Container: service
    Container->>Container: instance = service
    Container->>Container: supplier = null
    Container-->>App: service

    App->>Container: getInstance()
    Container-->>App: same service
```

```puml
| Implementation | Lazy | Thread-safe | Synchronization after initialization | Runtime configuration | Serialization identity |
|---|---:|---:|---:|---:|---:|
| Basic | No | Yes | No | No | No |
| Eager | No | Yes | No | No | No |
| Lazy | Yes | No | No | No | No |
| Synchronized | Yes | Yes | Every access | No | No |
| Double-checked | Yes | Yes | No | No | No |
| Holder | Yes | Yes | No | No | No |
| Enum | No | Yes | No | No | Yes |
| Supplier-backed | Yes | Yes | No | Yes | No |
```

## Implementations

### Completed

- Basic Singleton
- Eager Singleton
- Lazy Singleton
- Synchronized Singleton
- Double-checked Locking Singleton
- Initialization-on-demand Holder
- Enum Singleton
- Supplier-backed Singleton

### Additional investigations

- Reflection resistance
- Serialization with readResolve
- ClassLoader-scoped Singleton
- Singleton lifecycle in Spring
- Dependency injection as an alternative
## Recommendations

### Prefer Holder Singleton when

- Initialization must be lazy.
- No runtime arguments are required.
- The lifecycle can follow the ClassLoader.
- A compact implementation is preferred.

### Prefer Enum Singleton when

- Serialization identity matters.
- One fixed instance is sufficient.
- Inheritance is not required.
- Runtime initialization arguments are unnecessary.

### Prefer Supplier-backed Singleton when

- The creation strategy is configured at runtime.
- Initialization must be deferred.
- The instance is expensive to create.
- A custom construction hook is required.

### Prefer Dependency Injection when

- The application already uses Spring.
- The object has dependencies.
- Lifecycle management matters.
- Test replacement is required.
- Multiple configurations or scopes are needed.

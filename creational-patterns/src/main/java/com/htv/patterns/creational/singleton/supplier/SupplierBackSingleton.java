package com.htv.patterns.creational.singleton.supplier;

import java.util.Objects;
import java.util.function.Supplier;

public final class SupplierBackSingleton<T> {

    static final String ALREADY_CONFIGURED_MESSAGE =
            "Singleton has already been configured";

    static final String NOT_CONFIGURED_MESSAGE =
            "Singleton has not been configured";

    private final Object monitor =
            new Object();

    private volatile Supplier<? extends T> supplier;
    private volatile T instance;

    public void configure(
            Supplier<? extends T> instanceSupplier
    ) {
        Objects.requireNonNull(
                instanceSupplier,
                "instanceSupplier must not be null"
        );

        synchronized (monitor) {
            if (supplier != null || instance != null) {
                throw new IllegalStateException(
                        ALREADY_CONFIGURED_MESSAGE
                );
            }

            supplier = instanceSupplier;
        }
    }

    public boolean isConfigured() {
        return supplier != null
                || instance != null;
    }

    public boolean isInitialized() {
        return instance != null;
    }

    public T getInstance() {
        T result = instance;

        if (result != null) {
            return result;
        }

        synchronized (monitor) {
            result = instance;

            if (result != null) {
                return result;
            }

            Supplier<? extends T>
                    currentSupplier = supplier;

            if (currentSupplier == null) {
                throw new IllegalStateException(
                        NOT_CONFIGURED_MESSAGE
                );
            }

            try {
                result = Objects.requireNonNull(
                        currentSupplier.get(),
                        "Singleton supplier returned null"
                );
            } catch (RuntimeException exception) {
                throw new SingletonInitializationException(
                        "Unable to initialize Singleton",
                        exception
                );
            }

            instance = result;

            /*
             * Release references captured by the Supplier
             * after successful initialization.
             */
            supplier = null;

            return result;
        }
    }

    void resetForTest() {
        synchronized (monitor) {
            instance = null;
            supplier = null;
        }
    }
}

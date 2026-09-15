package com.htv.patterns.creational.singleton.supplier;

import com.htv.patterns.creational.singleton.support.ConcurrentSingletonVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SupplierBackedSingletonTest {

    private SupplierBackSingleton<TestService>
            singleton;

    @BeforeEach
    void setUp() {
        singleton =
                new SupplierBackSingleton<>();
    }

    @Test
    void shouldRequireConfigurationBeforeAccess() {
        assertThatThrownBy(
                singleton::getInstance
        )
                .isInstanceOf(
                        IllegalStateException.class
                )
                .hasMessage(
                        "Singleton has not been configured"
                );
    }

    @Test
    void shouldRejectNullSupplier() {
        assertThatThrownBy(
                () -> singleton.configure(null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "instanceSupplier must not be null"
                );
    }

    @Test
    void shouldNotInitializeDuringConfiguration() {
        AtomicInteger calls =
                new AtomicInteger();

        singleton.configure(() -> {
            calls.incrementAndGet();

            return new TestService(
                    "configured"
            );
        });

        assertThat(singleton.isConfigured())
                .isTrue();

        assertThat(singleton.isInitialized())
                .isFalse();

        assertThat(calls)
                .hasValue(0);
    }

    @Test
    void shouldInitializeOnFirstAccess() {
        AtomicInteger calls =
                new AtomicInteger();

        singleton.configure(() -> {
            calls.incrementAndGet();

            return new TestService(
                    "configured"
            );
        });

        TestService service =
                singleton.getInstance();

        assertThat(service.name())
                .isEqualTo("configured");

        assertThat(singleton.isInitialized())
                .isTrue();

        assertThat(calls)
                .hasValue(1);
    }

    @Test
    void shouldReturnSameInstanceSequentially() {
        singleton.configure(
                () -> new TestService("service")
        );

        TestService first =
                singleton.getInstance();

        TestService second =
                singleton.getInstance();

        assertThat(first)
                .isSameAs(second);
    }

    @Test
    void shouldRejectSecondConfiguration() {
        singleton.configure(
                () -> new TestService("first")
        );

        assertThatThrownBy(
                () -> singleton.configure(
                        () -> new TestService("second")
                )
        )
                .isInstanceOf(
                        IllegalStateException.class
                )
                .hasMessage(
                        "Singleton has already been configured"
                );
    }

    @Test
    void shouldRejectConfigurationAfterInitialization() {
        singleton.configure(
                () -> new TestService("first")
        );

        singleton.getInstance();

        assertThatThrownBy(
                () -> singleton.configure(
                        () -> new TestService("second")
                )
        )
                .isInstanceOf(
                        IllegalStateException.class
                );
    }

    @Test
    void shouldWrapInitializationFailure() {
        singleton.configure(() -> {
            throw new IllegalArgumentException(
                    "Invalid configuration"
            );
        });

        assertThatThrownBy(
                singleton::getInstance
        )
                .isInstanceOf(
                        SingletonInitializationException.class
                )
                .hasMessage(
                        "Unable to initialize Singleton"
                )
                .hasCauseInstanceOf(
                        IllegalArgumentException.class
                );
    }

    @Test
    void shouldRejectNullResultFromSupplier() {
        singleton.configure(
                () -> null
        );

        assertThatThrownBy(
                singleton::getInstance
        )
                .isInstanceOf(
                        SingletonInitializationException.class
                )
                .hasCauseInstanceOf(
                        NullPointerException.class
                );
    }

    @Test
    @Timeout(
            value = 10,
            unit = TimeUnit.SECONDS
    )
    void shouldInvokeSupplierOnlyOnceForConcurrentCallers() {
        AtomicInteger calls =
                new AtomicInteger();

        singleton.configure(() -> {
            calls.incrementAndGet();

            return new TestService(
                    "concurrent"
            );
        });

        Set<TestService> instances =
                ConcurrentSingletonVerifier
                        .collectInstances(
                                singleton::getInstance,
                                64
                        );

        assertThat(instances)
                .hasSize(1);

        assertThat(calls)
                .hasValue(1);

        assertThat(singleton.isInitialized())
                .isTrue();
    }

    private record TestService(
            String name
    ) {
    }
}
package com.htv.patterns.behavioral.strategy.registry;

import com.htv.patterns.behavioral.strategy.basic.EmailNotificationStrategy;
import com.htv.patterns.behavioral.strategy.basic.SmsNotificationStrategy;
import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import com.htv.patterns.behavioral.strategy.core.NotificationStrategy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationStrategyRegistryBuilderTest {

    @Test
    void shouldBuildRegistryWithStrategies() {
        NotificationStrategyRegistry registry =
                new NotificationStrategyRegistryBuilder()
                        .register(
                                new EmailNotificationStrategy()
                        )
                        .register(
                                new SmsNotificationStrategy()
                        )
                        .build();

        assertThat(registry.size())
                .isEqualTo(2);

        assertThat(registry.registeredChannels())
                .containsExactlyInAnyOrder(
                        NotificationChannel.EMAIL,
                        NotificationChannel.SMS
                );
    }

    @Test
    void shouldRejectDuplicateStrategy() {
        NotificationStrategyRegistryBuilder builder =
                new NotificationStrategyRegistryBuilder()
                        .register(
                                new EmailNotificationStrategy()
                        );

        assertThatThrownBy(
                () -> builder.register(
                        new EmailNotificationStrategy()
                )
        )
                .isInstanceOf(
                        DuplicateStrategyException.class
                )
                .hasMessageContaining(
                        "EMAIL"
                );
    }

    @Test
    void shouldRejectNullStrategy() {
        NotificationStrategyRegistryBuilder builder =
                new NotificationStrategyRegistryBuilder();

        assertThatThrownBy(
                () -> builder.register(null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "strategy must not be null"
                );
    }

    @Test
    void shouldRejectStrategyWithNullChannel() {
        NotificationStrategy strategy =
                new NotificationStrategy() {

                    @Override
                    public NotificationChannel supports() {
                        return null;
                    }

                    @Override
                    public NotificationMessage execute(
                            NotificationRequest request
                    ) {
                        return null;
                    }
                };

        assertThatThrownBy(
                () -> new NotificationStrategyRegistryBuilder()
                        .register(strategy)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessageContaining(
                        "supports()"
                );
    }

    @Test
    void shouldRejectEmptyBuild() {
        assertThatThrownBy(
                () -> new NotificationStrategyRegistryBuilder()
                        .build()
        )
                .isInstanceOf(
                        IllegalStateException.class
                )
                .hasMessage(
                        "At least one strategy must be registered"
                );
    }

    @Test
    void shouldAllowExplicitOverride() {
        NotificationStrategy original =
                new EmailNotificationStrategy();

        NotificationStrategy replacement =
                new EmailNotificationStrategy();

        NotificationStrategyRegistry registry =
                new NotificationStrategyRegistryBuilder()
                        .register(original)
                        .override(replacement)
                        .build();

        assertThat(
                registry.resolve(
                        NotificationChannel.EMAIL
                )
        ).isSameAs(replacement);
    }
}
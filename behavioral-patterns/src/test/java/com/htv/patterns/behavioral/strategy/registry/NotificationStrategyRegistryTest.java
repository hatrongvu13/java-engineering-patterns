package com.htv.patterns.behavioral.strategy.registry;

import com.htv.patterns.behavioral.strategy.basic.EmailNotificationStrategy;
import com.htv.patterns.behavioral.strategy.basic.SmsNotificationStrategy;
import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationStrategy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationStrategyRegistryTest {

    @Test
    void shouldResolveRegisteredStrategy() {
        EmailNotificationStrategy emailStrategy =
                new EmailNotificationStrategy();

        NotificationStrategyRegistry registry =
                new NotificationStrategyRegistryBuilder()
                        .register(emailStrategy)
                        .build();

        NotificationStrategy result =
                registry.resolve(
                        NotificationChannel.EMAIL
                );

        assertThat(result)
                .isSameAs(emailStrategy);
    }

    @Test
    void shouldRejectUnregisteredChannel() {
        NotificationStrategyRegistry registry =
                new NotificationStrategyRegistryBuilder()
                        .register(
                                new EmailNotificationStrategy()
                        )
                        .build();

        assertThatThrownBy(
                () -> registry.resolve(
                        NotificationChannel.SMS
                )
        )
                .isInstanceOf(
                        StrategyNotFoundException.class
                )
                .hasMessageContaining(
                        "SMS"
                );
    }

    @Test
    void shouldExposeImmutableChannelSet() {
        NotificationStrategyRegistry registry =
                new NotificationStrategyRegistryBuilder()
                        .register(
                                new EmailNotificationStrategy()
                        )
                        .register(
                                new SmsNotificationStrategy()
                        )
                        .build();

        assertThatThrownBy(
                () -> registry
                        .registeredChannels()
                        .remove(
                                NotificationChannel.EMAIL
                        )
        )
                .isInstanceOf(
                        UnsupportedOperationException.class
                );
    }
}
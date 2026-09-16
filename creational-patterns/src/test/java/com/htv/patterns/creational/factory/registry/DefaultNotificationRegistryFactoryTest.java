package com.htv.patterns.creational.factory.registry;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.PushNotificationSender;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultNotificationRegistryFactoryTest {

    @Test
    void shouldRegisterAllDefaultTypes() {
        NotificationSenderRegistry registry =
                DefaultNotificationRegistryFactory
                        .create();

        assertThat(registry.size())
                .isEqualTo(3);

        assertThat(registry.registeredTypes())
                .containsExactlyInAnyOrder(
                        NotificationType.EMAIL,
                        NotificationType.SMS,
                        NotificationType.PUSH
                );
    }

    @ParameterizedTest
    @MethodSource("senderCases")
    void shouldCreateExpectedSender(
            NotificationType type,
            Class<? extends NotificationSender>
                    expectedSenderType
    ) {
        NotificationSenderRegistry registry =
                DefaultNotificationRegistryFactory
                        .create();

        NotificationSender sender =
                registry.create(type);

        assertThat(sender)
                .isInstanceOf(expectedSenderType);

        assertThat(sender.supports())
                .isEqualTo(type);
    }

    @Test
    void shouldCreateSeparateRegistries() {
        NotificationSenderRegistry first =
                DefaultNotificationRegistryFactory
                        .create();

        NotificationSenderRegistry second =
                DefaultNotificationRegistryFactory
                        .create();

        assertThat(first)
                .isNotSameAs(second);
    }

    private static Stream<Arguments>
    senderCases() {
        return Stream.of(
                Arguments.of(
                        NotificationType.EMAIL,
                        EmailNotificationSender.class
                ),
                Arguments.of(
                        NotificationType.SMS,
                        SmsNotificationSender.class
                ),
                Arguments.of(
                        NotificationType.PUSH,
                        PushNotificationSender.class
                )
        );
    }
}
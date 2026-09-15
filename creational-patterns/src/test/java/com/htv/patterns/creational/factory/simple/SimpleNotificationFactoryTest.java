package com.htv.patterns.creational.factory.simple;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.PushNotificationSender;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimpleNotificationFactoryTest {

    private SimpleNotificationFactory factory;

    @BeforeEach
    void setUp() {
        factory =
                new SimpleNotificationFactory();
    }

    @ParameterizedTest
    @MethodSource("supportedNotificationTypes")
    void shouldCreateExpectedSender(
            NotificationType type,
            Class<? extends NotificationSender>
                    expectedSenderType
    ) {
        NotificationSender sender =
                factory.create(type);

        assertThat(sender)
                .isInstanceOf(expectedSenderType);

        assertThat(sender.supports())
                .isEqualTo(type);
    }

    @Test
    void shouldRejectNullType() {
        assertThatThrownBy(
                () -> factory.create(null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "type must not be null"
                );
    }

    @Test
    void shouldCreateIndependentSenderObjects() {
        NotificationSender first =
                factory.create(
                        NotificationType.EMAIL
                );

        NotificationSender second =
                factory.create(
                        NotificationType.EMAIL
                );

        assertThat(first)
                .isNotSameAs(second);
    }

    private static Stream<Arguments>
    supportedNotificationTypes() {
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
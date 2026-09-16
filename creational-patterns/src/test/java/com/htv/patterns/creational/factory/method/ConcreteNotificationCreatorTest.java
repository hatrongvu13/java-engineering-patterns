package com.htv.patterns.creational.factory.method;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.PushNotificationSender;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ConcreteNotificationCreatorTest {

    @ParameterizedTest
    @MethodSource("creatorCases")
    void shouldCreateExpectedProduct(
            NotificationCreator creator,
            Class<? extends NotificationSender>
                    expectedClass,
            NotificationType expectedType
    ) {
        NotificationSender sender =
                creator.newSender();

        assertThat(sender)
                .isInstanceOf(expectedClass);

        assertThat(sender.supports())
                .isEqualTo(expectedType);
    }

    private static Stream<Arguments>
    creatorCases() {
        return Stream.of(
                Arguments.of(
                        new EmailNotificationCreator(),
                        EmailNotificationSender.class,
                        NotificationType.EMAIL
                ),
                Arguments.of(
                        new SmsNotificationCreator(),
                        SmsNotificationSender.class,
                        NotificationType.SMS
                ),
                Arguments.of(
                        new PushNotificationCreator(),
                        PushNotificationSender.class,
                        NotificationType.PUSH
                )
        );
    }
}
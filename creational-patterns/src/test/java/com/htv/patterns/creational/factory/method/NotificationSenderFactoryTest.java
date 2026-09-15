package com.htv.patterns.creational.factory.method;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.PushNotificationSender;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationSenderFactoryTest {

    @Test
    void shouldCreateEmailSender() {
        NotificationSender sender =
                NotificationSenderFactory.email();

        assertThat(sender)
                .isInstanceOf(
                        EmailNotificationSender.class
                );
    }

    @Test
    void shouldCreateSmsSender() {
        NotificationSender sender =
                NotificationSenderFactory.sms();

        assertThat(sender)
                .isInstanceOf(
                        SmsNotificationSender.class
                );
    }

    @Test
    void shouldCreatePushSender() {
        NotificationSender sender =
                NotificationSenderFactory.push();

        assertThat(sender)
                .isInstanceOf(
                        PushNotificationSender.class
                );
    }

    @ParameterizedTest
    @CsvSource({
            "email, EMAIL",
            "EMAIL, EMAIL",
            "' email ', EMAIL",
            "sms, SMS",
            "push, PUSH"
    })
    void shouldCreateSenderFromString(
            String input,
            NotificationType expectedType
    ) {
        NotificationSender sender =
                NotificationSenderFactory.from(
                        input
                );

        assertThat(sender.supports())
                .isEqualTo(expectedType);
    }

    @Test
    void shouldCreateSenderFromEnum() {
        NotificationSender sender =
                NotificationSenderFactory.from(
                        NotificationType.EMAIL
                );

        assertThat(sender.supports())
                .isEqualTo(
                        NotificationType.EMAIL
                );
    }

    @Test
    void shouldRejectNullStringType() {
        assertThatThrownBy(
                () -> NotificationSenderFactory
                        .from((String) null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "type must not be null"
                );
    }

    @Test
    void shouldRejectBlankStringType() {
        assertThatThrownBy(
                () -> NotificationSenderFactory
                        .from("  ")
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessage(
                        "type must not be blank"
                );
    }

    @Test
    void shouldRejectUnsupportedType() {
        assertThatThrownBy(
                () -> NotificationSenderFactory
                        .from("telegram")
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessageContaining(
                        "Unsupported notification type"
                );
    }
}
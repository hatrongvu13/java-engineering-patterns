package com.htv.patterns.creational.factory.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationSenderTest {

    @ParameterizedTest
    @MethodSource("senders")
    void shouldReturnSuccessfulResult(
            NotificationSender sender
    ) {
        NotificationRequest request =
                new NotificationRequest(
                        "recipient@example.com",
                        "Pattern catalog",
                        "Factory message"
                );

        NotificationResult result =
                sender.send(request);

        assertThat(result.type())
                .isEqualTo(sender.supports());

        assertThat(result.recipient())
                .isEqualTo(
                        request.recipient()
                );

        assertThat(result.messageId())
                .isNotBlank();

        assertThat(result.sentAt())
                .isNotNull();
    }

    @Test
    void shouldRejectSmsContentLongerThanLimit() {
        SmsNotificationSender sender =
                new SmsNotificationSender();

        NotificationRequest request =
                new NotificationRequest(
                        "+84943561685",
                        "",
                        "a".repeat(161)
                );

        assertThatThrownBy(
                () -> sender.send(request)
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessageContaining(
                        "must not exceed 160"
                );
    }

    private static Stream<NotificationSender>
    senders() {
        return Stream.of(
                new EmailNotificationSender(),
                new SmsNotificationSender(),
                new PushNotificationSender()
        );
    }
}
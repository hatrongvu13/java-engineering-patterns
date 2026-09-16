package com.htv.patterns.behavioral.strategy.registry;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationServiceTest {

    @Test
    void shouldChooseEmailStrategy() {
        NotificationService service =
                new NotificationService(
                        DefaultNotificationStrategyRegistry
                                .create()
                );

        NotificationMessage result =
                service.createMessage(
                        NotificationChannel.EMAIL,
                        new NotificationRequest(
                                "developer@example.com",
                                "Strategy",
                                "Email content"
                        )
                );

        assertThat(result.channel())
                .isEqualTo(
                        NotificationChannel.EMAIL
                );

        assertThat(result.contentType())
                .isEqualTo("text/html");
    }

    @Test
    void shouldChooseSmsStrategy() {
        NotificationService service =
                new NotificationService(
                        DefaultNotificationStrategyRegistry
                                .create()
                );

        NotificationMessage result =
                service.createMessage(
                        NotificationChannel.SMS,
                        new NotificationRequest(
                                "+84943561685",
                                "",
                                "SMS content"
                        )
                );

        assertThat(result.channel())
                .isEqualTo(
                        NotificationChannel.SMS
                );

        assertThat(result.contentType())
                .isEqualTo("text/plain");
    }

    @Test
    void shouldRejectNullRegistry() {
        assertThatThrownBy(
                () -> new NotificationService(null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "registry must not be null"
                );
    }
}
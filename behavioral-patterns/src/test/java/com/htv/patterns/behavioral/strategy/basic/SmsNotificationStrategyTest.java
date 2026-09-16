package com.htv.patterns.behavioral.strategy.basic;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SmsNotificationStrategyTest {

    private final SmsNotificationStrategy strategy =
            new SmsNotificationStrategy();

    @Test
    void shouldNormalizeSmsContent() {
        NotificationRequest request =
                new NotificationRequest(
                        "+84943561685",
                        "",
                        "Hello    from\nStrategy"
                );

        NotificationMessage message =
                strategy.execute(request);

        assertThat(message.channel())
                .isEqualTo(
                        NotificationChannel.SMS
                );

        assertThat(message.subject())
                .isEmpty();

        assertThat(message.body())
                .isEqualTo(
                        "Hello from Strategy"
                );

        assertThat(message.contentType())
                .isEqualTo("text/plain");
    }

    @Test
    void shouldRejectContentOverLimit() {
        NotificationRequest request =
                new NotificationRequest(
                        "+84943561685",
                        "",
                        "a".repeat(161)
                );

        assertThatThrownBy(
                () -> strategy.execute(request)
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessageContaining(
                        "must not exceed 160"
                );
    }
}
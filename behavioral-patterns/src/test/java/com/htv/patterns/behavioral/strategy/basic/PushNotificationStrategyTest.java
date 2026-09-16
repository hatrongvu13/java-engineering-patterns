package com.htv.patterns.behavioral.strategy.basic;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PushNotificationStrategyTest {

    private final PushNotificationStrategy strategy =
            new PushNotificationStrategy();

    @Test
    void shouldCreatePushMessage() {
        NotificationMessage message =
                strategy.execute(
                        new NotificationRequest(
                                "device-token-1234567890",
                                "Report ready",
                                "Your report is ready"
                        )
                );

        assertThat(message.channel())
                .isEqualTo(
                        NotificationChannel.PUSH
                );

        assertThat(message.contentType())
                .isEqualTo(
                        "application/push+json"
                );
    }

    @Test
    void shouldTruncateLongBody() {
        NotificationMessage message =
                strategy.execute(
                        new NotificationRequest(
                                "device-token-1234567890",
                                "Long message",
                                "a".repeat(300)
                        )
                );

        assertThat(message.body())
                .hasSize(240)
                .endsWith("…");
    }
}
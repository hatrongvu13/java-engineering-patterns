package com.htv.patterns.behavioral.strategy.context;

import com.htv.patterns.behavioral.strategy.basic.EmailNotificationStrategy;
import com.htv.patterns.behavioral.strategy.basic.SmsNotificationStrategy;
import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationContextTest {

    @Test
    void shouldExecuteInitialStrategy() {
        NotificationContext context =
                new NotificationContext(
                        new EmailNotificationStrategy()
                );

        NotificationMessage message =
                context.execute(
                        new NotificationRequest(
                                "developer@example.com",
                                "Strategy",
                                "Email content"
                        )
                );

        assertThat(message.channel())
                .isEqualTo(
                        NotificationChannel.EMAIL
                );
    }

    @Test
    void shouldReplaceStrategyAtRuntime() {
        NotificationContext context =
                new NotificationContext(
                        new EmailNotificationStrategy()
                );

        context.setStrategy(
                new SmsNotificationStrategy()
        );

        NotificationMessage message =
                context.execute(
                        new NotificationRequest(
                                "+84943561685",
                                "",
                                "SMS content"
                        )
                );

        assertThat(message.channel())
                .isEqualTo(
                        NotificationChannel.SMS
                );
    }

    @Test
    void shouldRejectNullStrategy() {
        assertThatThrownBy(
                () -> new NotificationContext(null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "strategy must not be null"
                );
    }
}
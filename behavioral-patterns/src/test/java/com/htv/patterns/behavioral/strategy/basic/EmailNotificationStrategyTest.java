package com.htv.patterns.behavioral.strategy.basic;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailNotificationStrategyTest {

    private final EmailNotificationStrategy strategy =
            new EmailNotificationStrategy();

    @Test
    void shouldCreateHtmlEmailMessage() {
        NotificationRequest request =
                new NotificationRequest(
                        "developer@example.com",
                        "Strategy Pattern",
                        "Email content"
                );

        NotificationMessage message =
                strategy.execute(request);

        assertThat(message.channel())
                .isEqualTo(
                        NotificationChannel.EMAIL
                );

        assertThat(message.contentType())
                .isEqualTo("text/html");

        assertThat(message.subject())
                .isEqualTo(
                        "Strategy Pattern"
                );

        assertThat(message.body())
                .contains(
                        "<h1>Strategy Pattern</h1>"
                )
                .contains(
                        "<p>Email content</p>"
                );
    }

    @Test
    void shouldEscapeHtmlContent() {
        NotificationRequest request =
                new NotificationRequest(
                        "developer@example.com",
                        "<Admin>",
                        "<script>alert('test')</script>"
                );

        NotificationMessage message =
                strategy.execute(request);

        assertThat(message.body())
                .doesNotContain("<script>")
                .contains("&lt;script&gt;")
                .contains("&lt;Admin&gt;");
    }

    @Test
    void shouldRejectBlankSubject() {
        NotificationRequest request =
                new NotificationRequest(
                        "developer@example.com",
                        "",
                        "Content"
                );

        assertThatThrownBy(
                () -> strategy.execute(request)
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessage(
                        "Email subject must not be blank"
                );
    }
}
package com.htv.patterns.creational.factory.method;

import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationResult;
import com.htv.patterns.creational.factory.notification.NotificationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationServiceTest {

    @Test
    void shouldRejectNullCreator() {
        assertThatThrownBy(
                () -> new NotificationService(null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "creator must not be null"
                );
    }

    @Test
    void shouldSendEmailUsingEmailCreator() {
        NotificationService service =
                new NotificationService(
                        new EmailNotificationCreator()
                );

        NotificationRequest request =
                new NotificationRequest(
                        "developer@example.com",
                        "Factory Method",
                        "Email notification"
                );

        NotificationResult result =
                service.notify(request);

        assertThat(result.type())
                .isEqualTo(
                        NotificationType.EMAIL
                );

        assertThat(result.recipient())
                .isEqualTo(
                        "developer@example.com"
                );
    }

    @Test
    void shouldSendSmsUsingSmsCreator() {
        NotificationService service =
                new NotificationService(
                        new SmsNotificationCreator()
                );

        NotificationRequest request =
                new NotificationRequest(
                        "+84943561685",
                        "",
                        "SMS notification"
                );

        NotificationResult result =
                service.notify(request);

        assertThat(result.type())
                .isEqualTo(
                        NotificationType.SMS
                );
    }
}
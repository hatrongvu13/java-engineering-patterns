package com.htv.patterns.creational.factory.registry;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationResult;
import com.htv.patterns.creational.factory.notification.NotificationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RegistryNotificationServiceTest {

    @Test
    void shouldRejectNullRegistry() {
        assertThatThrownBy(
                () -> new RegistryNotificationService(
                        null
                )
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "registry must not be null"
                );
    }

    @Test
    void shouldSendUsingRegisteredSender() {
        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                EmailNotificationSender::new
                        )
                        .build();

        RegistryNotificationService service =
                new RegistryNotificationService(
                        registry
                );

        NotificationRequest request =
                new NotificationRequest(
                        "developer@example.com",
                        "Registry Factory",
                        "Created through a registered Supplier"
                );

        NotificationResult result =
                service.send(
                        NotificationType.EMAIL,
                        request
                );

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
    void shouldRejectNullRequest() {
        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                EmailNotificationSender::new
                        )
                        .build();

        RegistryNotificationService service =
                new RegistryNotificationService(
                        registry
                );

        assertThatThrownBy(
                () -> service.send(
                        NotificationType.EMAIL,
                        null
                )
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "request must not be null"
                );
    }

    @Test
    void shouldRejectNullNotificationType() {
        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                EmailNotificationSender::new
                        )
                        .build();

        RegistryNotificationService service =
                new RegistryNotificationService(
                        registry
                );

        NotificationRequest request =
                new NotificationRequest(
                        "developer@example.com",
                        "Registry Factory",
                        "Null type test"
                );

        assertThatThrownBy(
                () -> service.send(
                        null,
                        request
                )
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "type must not be null"
                );
    }

    @Test
    void shouldRejectUnregisteredType() {
        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                EmailNotificationSender::new
                        )
                        .build();

        RegistryNotificationService service =
                new RegistryNotificationService(
                        registry
                );

        NotificationRequest request =
                new NotificationRequest(
                        "+84943561685",
                        "",
                        "SMS message"
                );

        assertThatThrownBy(
                () -> service.send(
                        NotificationType.SMS,
                        request
                )
        )
                .isInstanceOf(
                        UnregisteredNotificationTypeException.class
                );
    }
}
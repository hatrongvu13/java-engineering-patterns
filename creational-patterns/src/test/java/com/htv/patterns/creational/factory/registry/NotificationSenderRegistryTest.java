package com.htv.patterns.creational.factory.registry;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationSenderRegistryTest {

    @Test
    void shouldCreateRegisteredSender() {
        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                EmailNotificationSender::new
                        )
                        .build();

        NotificationSender sender =
                registry.create(
                        NotificationType.EMAIL
                );

        assertThat(sender)
                .isInstanceOf(
                        EmailNotificationSender.class
                );

        assertThat(sender.supports())
                .isEqualTo(
                        NotificationType.EMAIL
                );
    }

    @Test
    void shouldInvokeSupplierForEachCreation() {
        AtomicInteger creationCount =
                new AtomicInteger();

        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                () -> {
                                    creationCount.incrementAndGet();

                                    return new EmailNotificationSender();
                                }
                        )
                        .build();

        NotificationSender first =
                registry.create(
                        NotificationType.EMAIL
                );

        NotificationSender second =
                registry.create(
                        NotificationType.EMAIL
                );

        assertThat(first)
                .isNotSameAs(second);

        assertThat(creationCount)
                .hasValue(2);
    }

    @Test
    void shouldExposeRegisteredTypes() {
        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                EmailNotificationSender::new
                        )
                        .register(
                                NotificationType.SMS,
                                SmsNotificationSender::new
                        )
                        .build();

        assertThat(registry.size())
                .isEqualTo(2);

        assertThat(registry.registeredTypes())
                .containsExactlyInAnyOrder(
                        NotificationType.EMAIL,
                        NotificationType.SMS
                );

        assertThat(
                registry.contains(
                        NotificationType.EMAIL
                )
        ).isTrue();
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

        assertThatThrownBy(
                () -> registry.create(
                        NotificationType.SMS
                )
        )
                .isInstanceOf(
                        UnregisteredNotificationTypeException.class
                )
                .hasMessageContaining(
                        "SMS"
                );
    }

    @Test
    void shouldRejectNullType() {
        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                EmailNotificationSender::new
                        )
                        .build();

        assertThatThrownBy(
                () -> registry.create(null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "type must not be null"
                );
    }

    @Test
    void shouldRejectNullSenderFromSupplier() {
        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                () -> null
                        )
                        .build();

        assertThatThrownBy(
                () -> registry.create(
                        NotificationType.EMAIL
                )
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "Registered sender factory must not return null"
                );
    }

    @Test
    void shouldRejectIncompatibleSender() {
        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                SmsNotificationSender::new
                        )
                        .build();

        assertThatThrownBy(
                () -> registry.create(
                        NotificationType.EMAIL
                )
        )
                .isInstanceOf(
                        IllegalStateException.class
                )
                .hasMessageContaining(
                        "registered for EMAIL"
                )
                .hasMessageContaining(
                        "supporting SMS"
                );
    }

    @Test
    void shouldExposeUnmodifiableRegisteredTypes() {
        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                EmailNotificationSender::new
                        )
                        .build();

        assertThatThrownBy(
                () -> registry
                        .registeredTypes()
                        .remove(NotificationType.EMAIL)
        )
                .isInstanceOf(
                        UnsupportedOperationException.class
                );

        assertThat(
                registry.contains(
                        NotificationType.EMAIL
                )
        ).isTrue();
    }
}
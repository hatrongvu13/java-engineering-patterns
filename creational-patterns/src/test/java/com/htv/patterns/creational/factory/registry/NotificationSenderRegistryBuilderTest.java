package com.htv.patterns.creational.factory.registry;

import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationSenderRegistryBuilderTest {

    @Test
    void shouldRejectNullType() {
        NotificationSenderRegistryBuilder builder =
                new NotificationSenderRegistryBuilder();

        assertThatThrownBy(
                () -> builder.register(
                        null,
                        EmailNotificationSender::new
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
    void shouldRejectNullSupplier() {
        NotificationSenderRegistryBuilder builder =
                new NotificationSenderRegistryBuilder();

        assertThatThrownBy(
                () -> builder.register(
                        NotificationType.EMAIL,
                        null
                )
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "senderFactory must not be null"
                );
    }

    @Test
    void shouldRejectDuplicateRegistration() {
        NotificationSenderRegistryBuilder builder =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                EmailNotificationSender::new
                        );

        assertThatThrownBy(
                () -> builder.register(
                        NotificationType.EMAIL,
                        EmailNotificationSender::new
                )
        )
                .isInstanceOf(
                        DuplicateNotificationTypeException.class
                )
                .hasMessageContaining(
                        "already registered"
                )
                .hasMessageContaining(
                        "EMAIL"
                );
    }

    @Test
    void shouldRejectEmptyBuild() {
        NotificationSenderRegistryBuilder builder =
                new NotificationSenderRegistryBuilder();

        assertThatThrownBy(
                builder::build
        )
                .isInstanceOf(
                        IllegalStateException.class
                )
                .hasMessage(
                        "At least one sender factory must be registered"
                );
    }

    @Test
    void shouldSupportFluentRegistration() {
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
    }

    @Test
    void shouldAllowExplicitOverride() {
        AtomicSenderFactory firstFactory =
                new AtomicSenderFactory();

        AtomicSenderFactory secondFactory =
                new AtomicSenderFactory();

        NotificationSenderRegistry registry =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                firstFactory::createEmailSender
                        )
                        .override(
                                NotificationType.EMAIL,
                                secondFactory::createEmailSender
                        )
                        .build();

        registry.create(
                NotificationType.EMAIL
        );

        assertThat(firstFactory.invocationCount())
                .isZero();

        assertThat(secondFactory.invocationCount())
                .isEqualTo(1);
    }

    @Test
    void shouldCreateIndependentSnapshots() {
        NotificationSenderRegistryBuilder builder =
                new NotificationSenderRegistryBuilder()
                        .register(
                                NotificationType.EMAIL,
                                EmailNotificationSender::new
                        );

        NotificationSenderRegistry firstRegistry =
                builder.build();

        builder.register(
                NotificationType.SMS,
                SmsNotificationSender::new
        );

        NotificationSenderRegistry secondRegistry =
                builder.build();

        assertThat(
                firstRegistry.registeredTypes()
        )
                .containsExactly(
                        NotificationType.EMAIL
                );

        assertThat(
                secondRegistry.registeredTypes()
        )
                .containsExactlyInAnyOrder(
                        NotificationType.EMAIL,
                        NotificationType.SMS
                );
    }

    private static final class AtomicSenderFactory {

        private int invocationCount;

        EmailNotificationSender createEmailSender() {
            invocationCount++;

            return new EmailNotificationSender();
        }

        int invocationCount() {
            return invocationCount;
        }
    }
}
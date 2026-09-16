package com.htv.patterns.creational.factory.abstractfactory;

import com.htv.patterns.creational.factory.abstractfactory.core.FormattedNotification;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationComponentFactory;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationFormatter;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationProcessor;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationValidator;
import com.htv.patterns.creational.factory.abstractfactory.email.EmailNotificationComponentFactory;
import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationResult;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationProcessorTest {

    @Test
    void shouldRejectNullFactory() {
        assertThatThrownBy(
                () -> new NotificationProcessor(null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "factory must not be null"
                );
    }

    @Test
    void shouldProcessEmailWithCompatibleFamily() {
        NotificationProcessor processor =
                new NotificationProcessor(
                        new EmailNotificationComponentFactory()
                );

        NotificationResult result =
                processor.process(
                        new NotificationRequest(
                                "developer@example.com",
                                "Abstract Factory",
                                "Compatible product family"
                        )
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
    void shouldRejectInvalidEmailRequest() {
        NotificationProcessor processor =
                new NotificationProcessor(
                        new EmailNotificationComponentFactory()
                );

        NotificationRequest request =
                new NotificationRequest(
                        "invalid-email",
                        "Subject",
                        "Content"
                );

        assertThatThrownBy(
                () -> processor.process(request)
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessageContaining(
                        "Invalid email address"
                );
    }

    @Test
    void shouldRejectIncompatibleProductFamily() {
        NotificationComponentFactory factory =
                new NotificationComponentFactory() {

                    @Override
                    public NotificationType supports() {
                        return NotificationType.EMAIL;
                    }

                    @Override
                    public NotificationValidator
                    createValidator() {
                        return new StubValidator(
                                NotificationType.EMAIL
                        );
                    }

                    @Override
                    public NotificationFormatter
                    createFormatter() {
                        return new StubFormatter(
                                NotificationType.SMS
                        );
                    }

                    @Override
                    public NotificationSender
                    createSender() {
                        return new StubSender(
                                NotificationType.EMAIL
                        );
                    }
                };

        assertThatThrownBy(
                () -> new NotificationProcessor(factory)
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessageContaining(
                        "Incompatible formatter"
                );
    }

    @Test
    void shouldRejectNullValidatorFromFactory() {
        NotificationComponentFactory factory =
                new NullProductFactory(
                        null,
                        new StubFormatter(
                                NotificationType.EMAIL
                        ),
                        new StubSender(
                                NotificationType.EMAIL
                        )
                );

        assertThatThrownBy(
                () -> new NotificationProcessor(factory)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "Factory must not return a null validator"
                );
    }

    private record StubValidator(
            NotificationType supports
    ) implements NotificationValidator {

        @Override
        public void validate(
                NotificationRequest request
        ) {
        }
    }

    private record StubFormatter(
            NotificationType supports
    ) implements NotificationFormatter {

        @Override
        public FormattedNotification format(
                NotificationRequest request
        ) {
            return new FormattedNotification(
                    supports,
                    request.recipient(),
                    request.subject(),
                    request.content(),
                    "text/plain"
            );
        }
    }

    private record StubSender(
            NotificationType supports
    ) implements NotificationSender {

        @Override
        public NotificationResult send(
                NotificationRequest request
        ) {
            return new NotificationResult(
                    supports,
                    request.recipient(),
                    "STUB-001",
                    Instant.now()
            );
        }
    }

    private record NullProductFactory(
            NotificationValidator validator,
            NotificationFormatter formatter,
            NotificationSender sender
    ) implements NotificationComponentFactory {

        @Override
        public NotificationType supports() {
            return NotificationType.EMAIL;
        }

        @Override
        public NotificationValidator
        createValidator() {
            return validator;
        }

        @Override
        public NotificationFormatter
        createFormatter() {
            return formatter;
        }

        @Override
        public NotificationSender
        createSender() {
            return sender;
        }
    }
}
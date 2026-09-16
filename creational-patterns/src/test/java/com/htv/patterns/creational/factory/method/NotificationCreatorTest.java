package com.htv.patterns.creational.factory.method;

import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationResult;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationCreatorTest {

    @Test
    void shouldRejectNullRequest() {
        NotificationCreator creator =
                new StubNotificationCreator(
                        new StubNotificationSender()
                );

        assertThatThrownBy(
                () -> creator.send(null)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "request must not be null"
                );
    }

    @Test
    void shouldRejectNullProductFromFactoryMethod() {
        NotificationCreator creator =
                new NotificationCreator() {

                    @Override
                    protected NotificationSender
                    createSender() {
                        return null;
                    }
                };

        NotificationRequest request =
                validRequest();

        assertThatThrownBy(
                () -> creator.send(request)
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "Factory Method must not return null"
                );
    }

    @Test
    void shouldDelegateSendingToCreatedProduct() {
        StubNotificationSender sender =
                new StubNotificationSender();

        NotificationCreator creator =
                new StubNotificationCreator(sender);

        NotificationRequest request =
                validRequest();

        NotificationResult result =
                creator.send(request);

        assertThat(sender.invocationCount())
                .isEqualTo(1);

        assertThat(sender.lastRequest())
                .isSameAs(request);

        assertThat(result.type())
                .isEqualTo(
                        NotificationType.EMAIL
                );
    }

    @Test
    void shouldCreateNewProductForEachRequest() {
        AtomicInteger creations =
                new AtomicInteger();

        NotificationCreator creator =
                new NotificationCreator() {

                    @Override
                    protected NotificationSender
                    createSender() {
                        creations.incrementAndGet();

                        return new StubNotificationSender();
                    }
                };

        creator.send(validRequest());
        creator.send(validRequest());

        assertThat(creations)
                .hasValue(2);
    }

    @Test
    void shouldExecuteExtensionHooks() {
        AtomicBoolean beforeCalled =
                new AtomicBoolean();

        AtomicBoolean afterCalled =
                new AtomicBoolean();

        NotificationCreator creator =
                new NotificationCreator() {

                    @Override
                    protected NotificationSender
                    createSender() {
                        return new StubNotificationSender();
                    }

                    @Override
                    protected void beforeSend(
                            NotificationRequest request
                    ) {
                        beforeCalled.set(true);
                    }

                    @Override
                    protected void afterSend(
                            NotificationRequest request,
                            NotificationResult result
                    ) {
                        afterCalled.set(true);
                    }
                };

        creator.send(validRequest());

        assertThat(beforeCalled)
                .isTrue();

        assertThat(afterCalled)
                .isTrue();
    }

    private static NotificationRequest
    validRequest() {
        return new NotificationRequest(
                "developer@example.com",
                "Factory Method",
                "Factory Method test"
        );
    }

    private static final class
    StubNotificationCreator
            extends NotificationCreator {

        private final NotificationSender sender;

        private StubNotificationCreator(
                NotificationSender sender
        ) {
            this.sender = sender;
        }

        @Override
        protected NotificationSender
        createSender() {
            return sender;
        }
    }

    private static final class
    StubNotificationSender
            implements NotificationSender {

        private int invocationCount;
        private NotificationRequest lastRequest;

        @Override
        public NotificationType supports() {
            return NotificationType.EMAIL;
        }

        @Override
        public NotificationResult send(
                NotificationRequest request
        ) {
            invocationCount++;
            lastRequest = request;

            return new NotificationResult(
                    supports(),
                    request.recipient(),
                    "STUB-001",
                    Instant.now()
            );
        }

        int invocationCount() {
            return invocationCount;
        }

        NotificationRequest lastRequest() {
            return lastRequest;
        }
    }
}
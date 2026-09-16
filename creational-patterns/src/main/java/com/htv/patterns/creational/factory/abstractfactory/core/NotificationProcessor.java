package com.htv.patterns.creational.factory.abstractfactory.core;

import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationResult;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;

/**
 * Client of the Abstract Factory.
 *
 * <p>This processor works exclusively with abstract products and
 * does not depend on email, SMS, or push implementations.</p>
 */
public final class NotificationProcessor {

    private final NotificationType supportedType;
    private final NotificationValidator validator;
    private final NotificationFormatter formatter;
    private final NotificationSender sender;

    public NotificationProcessor(
            NotificationComponentFactory factory
    ) {
        Objects.requireNonNull(
                factory,
                "factory must not be null"
        );

        this.supportedType =
                Objects.requireNonNull(
                        factory.supports(),
                        "factory.supports() must not return null"
                );

        this.validator =
                Objects.requireNonNull(
                        factory.createValidator(),
                        "Factory must not return a null validator"
                );

        this.formatter =
                Objects.requireNonNull(
                        factory.createFormatter(),
                        "Factory must not return a null formatter"
                );

        this.sender =
                Objects.requireNonNull(
                        factory.createSender(),
                        "Factory must not return a null sender"
                );

        validateProductFamily();
    }

    public NotificationResult process(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        validator.validate(request);

        FormattedNotification formatted =
                Objects.requireNonNull(
                        formatter.format(request),
                        "Formatter must not return null"
                );

        NotificationRequest formattedRequest =
                new NotificationRequest(
                        formatted.recipient(),
                        formatted.subject(),
                        formatted.body()
                );

        return Objects.requireNonNull(
                sender.send(formattedRequest),
                "Sender must not return null"
        );
    }

    private void validateProductFamily() {
        requireCompatibleType(
                "validator",
                validator.supports()
        );

        requireCompatibleType(
                "formatter",
                formatter.supports()
        );

        requireCompatibleType(
                "sender",
                sender.supports()
        );
    }

    private void requireCompatibleType(
            String componentName,
            NotificationType componentType
    ) {
        Objects.requireNonNull(
                componentType,
                componentName
                        + ".supports() must not return null"
        );

        if (componentType != supportedType) {
            throw new IllegalArgumentException(
                    "Incompatible "
                            + componentName
                            + ": factory supports "
                            + supportedType
                            + " but component supports "
                            + componentType
            );
        }
    }
}

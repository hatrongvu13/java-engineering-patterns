package com.htv.patterns.creational.factory.method;

import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationResult;
import com.htv.patterns.creational.factory.notification.NotificationSender;

import java.util.Objects;

public abstract class NotificationCreator {

    /**
     * Factory Method implemented by concrete creators.
     */
    protected abstract NotificationSender createSender();

    public final NotificationResult send(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        NotificationSender sender =
                Objects.requireNonNull(
                        createSender(),
                        "Factory Method must not return null"
                );

        validateSender(sender);

        beforeSend(request);

        NotificationResult result =
                Objects.requireNonNull(
                        sender.send(request),
                        "NotificationSender.send() must not return null"
                );

        afterSend(request, result);

        return result;
    }

    public final NotificationSender newSender() {
        NotificationSender sender =
                Objects.requireNonNull(
                        createSender(),
                        "Factory Method must not return null"
                );

        validateSender(sender);

        return sender;
    }

    protected void beforeSend(
            NotificationRequest request
    ) {
        // Optional extension hook.
    }

    protected void afterSend(
            NotificationRequest request,
            NotificationResult result
    ) {
        // Optional extension hook.
    }

    private void validateSender(
            NotificationSender sender
    ) {
        Objects.requireNonNull(
                sender.supports(),
                "NotificationSender.supports() must not return null"
        );
    }
}
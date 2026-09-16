package com.htv.patterns.creational.factory.method;

import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationResult;

import java.util.Objects;

/**
 * Application service depending on the creator abstraction.
 */
public final class NotificationService {

    private final NotificationCreator creator;

    public NotificationService(
            NotificationCreator creator
    ) {
        this.creator =
                Objects.requireNonNull(
                        creator,
                        "creator must not be null"
                );
    }

    public NotificationResult notify(
            NotificationRequest request
    ) {
        return creator.send(request);
    }
}
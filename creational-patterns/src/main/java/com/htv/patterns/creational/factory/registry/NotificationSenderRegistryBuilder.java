package com.htv.patterns.creational.factory.registry;

import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Mutable bootstrap configuration for NotificationSenderRegistry.
 *
 * <p>The builder is used during application startup. The registry
 * returned by build() is an immutable snapshot.</p>
 */
public final class NotificationSenderRegistryBuilder {

    private final Map<
            NotificationType,
            Supplier<? extends NotificationSender>
            > factories =
            new EnumMap<>(NotificationType.class);

    /**
     * Registers a sender creation function.
     *
     * @throws DuplicateNotificationTypeException when the type has
     * already been registered
     */
    public NotificationSenderRegistryBuilder register(
            NotificationType type,
            Supplier<? extends NotificationSender>
                    senderFactory
    ) {
        validateRegistration(
                type,
                senderFactory
        );

        if (factories.containsKey(type)) {
            throw new DuplicateNotificationTypeException(
                    type
            );
        }

        factories.put(
                type,
                senderFactory
        );

        return this;
    }

    /**
     * Explicitly replaces an existing registration or creates a new
     * registration.
     *
     * <p>This operation should only be used during bootstrap or in
     * tests.</p>
     */
    public NotificationSenderRegistryBuilder override(
            NotificationType type,
            Supplier<? extends NotificationSender>
                    senderFactory
    ) {
        validateRegistration(
                type,
                senderFactory
        );

        factories.put(
                type,
                senderFactory
        );

        return this;
    }

    public boolean contains(
            NotificationType type
    ) {
        Objects.requireNonNull(
                type,
                "type must not be null"
        );

        return factories.containsKey(type);
    }

    public int registrationCount() {
        return factories.size();
    }

    /**
     * Creates an immutable registry snapshot.
     */
    public NotificationSenderRegistry build() {
        if (factories.isEmpty()) {
            throw new IllegalStateException(
                    "At least one sender factory must be registered"
            );
        }

        return new NotificationSenderRegistry(
                factories
        );
    }

    private static void validateRegistration(
            NotificationType type,
            Supplier<? extends NotificationSender>
                    senderFactory
    ) {
        Objects.requireNonNull(
                type,
                "type must not be null"
        );

        Objects.requireNonNull(
                senderFactory,
                "senderFactory must not be null"
        );
    }
}
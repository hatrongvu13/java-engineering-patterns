package com.htv.patterns.creational.factory.registry;

import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Immutable Registry-based Factory for notification senders.
 *
 * <p>Each notification type is associated with a Supplier that
 * creates a compatible NotificationSender.</p>
 *
 * <p>Use NotificationSenderRegistryBuilder to configure and create
 * instances of this registry.</p>
 */
public final class NotificationSenderRegistry {

    private final Map<
            NotificationType,
            Supplier<? extends NotificationSender>
            > factories;

    /*
     * Package-private constructor ensures that normal clients create
     * registries through NotificationSenderRegistryBuilder.
     */
    NotificationSenderRegistry(
            Map<
                    NotificationType,
                    Supplier<? extends NotificationSender>
                    > sourceFactories
    ) {
        Objects.requireNonNull(
                sourceFactories,
                "sourceFactories must not be null"
        );

        if (sourceFactories.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one sender factory must be registered"
            );
        }

        EnumMap<
                NotificationType,
                Supplier<? extends NotificationSender>
                > copiedFactories =
                new EnumMap<>(NotificationType.class);

        copiedFactories.putAll(sourceFactories);

        this.factories =
                Collections.unmodifiableMap(
                        copiedFactories
                );
    }

    /**
     * Creates a new sender for the requested notification type.
     *
     * @param type requested notification type
     * @return newly created and compatible sender
     */
    public NotificationSender create(
            NotificationType type
    ) {
        Objects.requireNonNull(
                type,
                "type must not be null"
        );

        Supplier<? extends NotificationSender>
                senderFactory = factories.get(type);

        if (senderFactory == null) {
            throw new UnregisteredNotificationTypeException(
                    type
            );
        }

        NotificationSender sender =
                Objects.requireNonNull(
                        senderFactory.get(),
                        "Registered sender factory must not return null"
                );

        validateCompatibility(
                type,
                sender
        );

        return sender;
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

    public int size() {
        return factories.size();
    }

    public Set<NotificationType> registeredTypes() {
        return factories.keySet();
    }

    private static void validateCompatibility(
            NotificationType registeredType,
            NotificationSender sender
    ) {
        NotificationType supportedType =
                Objects.requireNonNull(
                        sender.supports(),
                        "NotificationSender.supports() must not return null"
                );

        if (supportedType != registeredType) {
            throw new IllegalStateException(
                    "Sender factory registered for "
                            + registeredType
                            + " produced a sender supporting "
                            + supportedType
            );
        }
    }
}
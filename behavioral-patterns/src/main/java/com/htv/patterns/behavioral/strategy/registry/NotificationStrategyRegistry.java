package com.htv.patterns.behavioral.strategy.registry;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationStrategy;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable registry of notification strategies.
 */
public final class NotificationStrategyRegistry {

    private final Map<
            NotificationChannel,
            NotificationStrategy
            > strategies;

    NotificationStrategyRegistry(
            Map<
                    NotificationChannel,
                    NotificationStrategy
                    > sourceStrategies
    ) {
        Objects.requireNonNull(
                sourceStrategies,
                "sourceStrategies must not be null"
        );

        if (sourceStrategies.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one strategy must be registered"
            );
        }

        EnumMap<
                NotificationChannel,
                NotificationStrategy
                > copiedStrategies =
                new EnumMap<>(
                        NotificationChannel.class
                );

        copiedStrategies.putAll(
                sourceStrategies
        );

        this.strategies =
                Collections.unmodifiableMap(
                        copiedStrategies
                );
    }

    public NotificationStrategy resolve(
            NotificationChannel channel
    ) {
        Objects.requireNonNull(
                channel,
                "channel must not be null"
        );

        NotificationStrategy strategy =
                strategies.get(channel);

        if (strategy == null) {
            throw new StrategyNotFoundException(
                    channel
            );
        }

        validateCompatibility(
                channel,
                strategy
        );

        return strategy;
    }

    public boolean contains(
            NotificationChannel channel
    ) {
        Objects.requireNonNull(
                channel,
                "channel must not be null"
        );

        return strategies.containsKey(
                channel
        );
    }

    public int size() {
        return strategies.size();
    }

    public Set<NotificationChannel>
    registeredChannels() {
        return strategies.keySet();
    }

    private static void validateCompatibility(
            NotificationChannel registeredChannel,
            NotificationStrategy strategy
    ) {
        NotificationChannel supportedChannel =
                Objects.requireNonNull(
                        strategy.supports(),
                        "NotificationStrategy.supports() "
                                + "must not return null"
                );

        if (
                supportedChannel
                        != registeredChannel
        ) {
            throw new IllegalStateException(
                    "Strategy registered for "
                            + registeredChannel
                            + " supports "
                            + supportedChannel
            );
        }
    }
}
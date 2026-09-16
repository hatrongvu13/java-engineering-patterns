package com.htv.patterns.behavioral.strategy.registry;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationStrategy;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class NotificationStrategyRegistryBuilder {

    private final Map<
            NotificationChannel,
            NotificationStrategy
            > strategies =
            new EnumMap<>(
                    NotificationChannel.class
            );

    public NotificationStrategyRegistryBuilder register(
            NotificationStrategy strategy
    ) {
        Objects.requireNonNull(
                strategy,
                "strategy must not be null"
        );

        NotificationChannel channel =
                Objects.requireNonNull(
                        strategy.supports(),
                        "NotificationStrategy.supports() "
                                + "must not return null"
                );

        if (strategies.containsKey(channel)) {
            throw new DuplicateStrategyException(
                    channel
            );
        }

        strategies.put(
                channel,
                strategy
        );

        return this;
    }

    public NotificationStrategyRegistryBuilder override(
            NotificationStrategy strategy
    ) {
        Objects.requireNonNull(
                strategy,
                "strategy must not be null"
        );

        NotificationChannel channel =
                Objects.requireNonNull(
                        strategy.supports(),
                        "NotificationStrategy.supports() "
                                + "must not return null"
                );

        strategies.put(
                channel,
                strategy
        );

        return this;
    }

    public int registrationCount() {
        return strategies.size();
    }

    public NotificationStrategyRegistry build() {
        if (strategies.isEmpty()) {
            throw new IllegalStateException(
                    "At least one strategy must be registered"
            );
        }

        return new NotificationStrategyRegistry(
                strategies
        );
    }
}
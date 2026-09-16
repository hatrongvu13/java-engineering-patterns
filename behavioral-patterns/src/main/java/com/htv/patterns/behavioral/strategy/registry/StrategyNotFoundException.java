package com.htv.patterns.behavioral.strategy.registry;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;

import java.util.Objects;

public final class StrategyNotFoundException
        extends RuntimeException {

    public StrategyNotFoundException(
            NotificationChannel channel
    ) {
        super(
                "No notification strategy is registered "
                        + "for channel: "
                        + Objects.requireNonNull(
                        channel,
                        "channel must not be null"
                )
        );
    }
}
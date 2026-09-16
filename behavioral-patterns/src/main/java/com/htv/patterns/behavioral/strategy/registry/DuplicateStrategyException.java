package com.htv.patterns.behavioral.strategy.registry;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;

import java.util.Objects;

public final class DuplicateStrategyException
        extends RuntimeException {

    public DuplicateStrategyException(
            NotificationChannel channel
    ) {
        super(
                "A notification strategy is already registered "
                        + "for channel: "
                        + Objects.requireNonNull(
                        channel,
                        "channel must not be null"
                )
        );
    }
}
package com.htv.patterns.behavioral.strategy.registry;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import com.htv.patterns.behavioral.strategy.core.NotificationStrategy;

import java.util.Objects;

public final class NotificationService {

    private final NotificationStrategyRegistry registry;

    public NotificationService(
            NotificationStrategyRegistry registry
    ) {
        this.registry =
                Objects.requireNonNull(
                        registry,
                        "registry must not be null"
                );
    }

    public NotificationMessage createMessage(
            NotificationChannel channel,
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                channel,
                "channel must not be null"
        );

        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        NotificationStrategy strategy =
                registry.resolve(channel);

        NotificationMessage message =
                strategy.execute(request);

        return Objects.requireNonNull(
                message,
                "NotificationStrategy.execute() "
                        + "must not return null"
        );
    }
}
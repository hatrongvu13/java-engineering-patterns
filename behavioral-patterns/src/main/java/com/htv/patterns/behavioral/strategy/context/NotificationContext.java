package com.htv.patterns.behavioral.strategy.context;

import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import com.htv.patterns.behavioral.strategy.core.NotificationStrategy;

import java.util.Objects;

/**
 * Context that delegates message creation to a replaceable
 * notification strategy.
 */
public final class NotificationContext {

    private NotificationStrategy strategy;

    public NotificationContext(
            NotificationStrategy strategy
    ) {
        setStrategy(strategy);
    }

    public void setStrategy(
            NotificationStrategy strategy
    ) {
        this.strategy =
                Objects.requireNonNull(
                        strategy,
                        "strategy must not be null"
                );
    }

    public NotificationMessage execute(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        NotificationMessage result =
                strategy.execute(request);

        return Objects.requireNonNull(
                result,
                "NotificationStrategy.execute() must not return null"
        );
    }

    public NotificationStrategy getStrategy() {
        return strategy;
    }
}
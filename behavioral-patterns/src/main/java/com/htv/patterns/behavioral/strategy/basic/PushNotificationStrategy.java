package com.htv.patterns.behavioral.strategy.basic;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import com.htv.patterns.behavioral.strategy.core.NotificationStrategy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class PushNotificationStrategy
        implements NotificationStrategy {

    private static final int MAXIMUM_BODY_LENGTH =
            240;

    @Override
    public NotificationChannel supports() {
        return NotificationChannel.PUSH;
    }

    @Override
    public NotificationMessage execute(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        String body = truncate(
                request.content()
        );

        Map<String, String> metadata =
                new LinkedHashMap<>(
                        request.attributes()
                );

        metadata.put(
                "strategy",
                "push"
        );

        return new NotificationMessage(
                supports(),
                request.recipient(),
                request.subject(),
                body,
                "application/push+json",
                metadata
        );
    }

    private static String truncate(
            String value
    ) {
        if (
                value.length()
                        <= MAXIMUM_BODY_LENGTH
        ) {
            return value;
        }

        return value.substring(
                0,
                MAXIMUM_BODY_LENGTH - 1
        ) + "…";
    }
}
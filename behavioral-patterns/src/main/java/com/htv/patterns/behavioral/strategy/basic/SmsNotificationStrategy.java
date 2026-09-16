package com.htv.patterns.behavioral.strategy.basic;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import com.htv.patterns.behavioral.strategy.core.NotificationStrategy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class SmsNotificationStrategy
        implements NotificationStrategy {

    private static final int MAXIMUM_LENGTH =
            160;

    @Override
    public NotificationChannel supports() {
        return NotificationChannel.SMS;
    }

    @Override
    public NotificationMessage execute(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        String normalizedContent =
                request.content()
                        .replaceAll("\\s+", " ")
                        .trim();

        if (
                normalizedContent.length()
                        > MAXIMUM_LENGTH
        ) {
            throw new IllegalArgumentException(
                    "SMS content must not exceed "
                            + MAXIMUM_LENGTH
                            + " characters"
            );
        }

        Map<String, String> metadata =
                new LinkedHashMap<>(
                        request.attributes()
                );

        metadata.put(
                "strategy",
                "sms"
        );

        metadata.put(
                "characterCount",
                String.valueOf(
                        normalizedContent.length()
                )
        );

        return new NotificationMessage(
                supports(),
                request.recipient(),
                "",
                normalizedContent,
                "text/plain",
                metadata
        );
    }
}
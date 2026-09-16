package com.htv.patterns.creational.factory.abstractfactory.push;

import com.htv.patterns.creational.factory.abstractfactory.core.FormattedNotification;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationFormatter;
import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;

public final class PushMessageFormatter
        implements NotificationFormatter {

    private static final int MAXIMUM_BODY_LENGTH =
            240;

    @Override
    public NotificationType supports() {
        return NotificationType.PUSH;
    }

    @Override
    public FormattedNotification format(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        String body = request.content();

        if (body.length() > MAXIMUM_BODY_LENGTH) {
            body = body.substring(
                    0,
                    MAXIMUM_BODY_LENGTH - 1
            ) + "…";
        }

        return new FormattedNotification(
                supports(),
                request.recipient(),
                request.subject(),
                body,
                "application/push+json"
        );
    }
}
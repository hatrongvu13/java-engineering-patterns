package com.htv.patterns.creational.factory.abstractfactory.sms;

import com.htv.patterns.creational.factory.abstractfactory.core.FormattedNotification;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationFormatter;
import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;

public final class PlainTextSmsFormatter
        implements NotificationFormatter {

    @Override
    public NotificationType supports() {
        return NotificationType.SMS;
    }

    @Override
    public FormattedNotification format(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        String normalizedBody =
                request.content()
                        .replaceAll("\\s+", " ")
                        .trim();

        return new FormattedNotification(
                supports(),
                request.recipient(),
                "",
                normalizedBody,
                "text/plain"
        );
    }
}
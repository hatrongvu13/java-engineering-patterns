package com.htv.patterns.creational.factory.abstractfactory.email;

import com.htv.patterns.creational.factory.abstractfactory.core.FormattedNotification;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationFormatter;
import com.htv.patterns.creational.factory.notification.NotificationRequest;
import com.htv.patterns.creational.factory.notification.NotificationType;

import java.util.Objects;

public final class HtmlEmailFormatter
        implements NotificationFormatter {

    @Override
    public NotificationType supports() {
        return NotificationType.EMAIL;
    }

    @Override
    public FormattedNotification format(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        String htmlBody = """
            <html>
              <body>
                <h1>%s</h1>
                <p>%s</p>
              </body>
            </html>
            """.formatted(
                escapeHtml(request.subject()),
                escapeHtml(request.content())
        );

        return new FormattedNotification(
                supports(),
                request.recipient(),
                request.subject(),
                htmlBody,
                "text/html"
        );
    }

    private static String escapeHtml(
            String value
    ) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
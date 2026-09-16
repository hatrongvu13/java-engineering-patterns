package com.htv.patterns.behavioral.strategy.basic;

import com.htv.patterns.behavioral.strategy.core.NotificationChannel;
import com.htv.patterns.behavioral.strategy.core.NotificationMessage;
import com.htv.patterns.behavioral.strategy.core.NotificationRequest;
import com.htv.patterns.behavioral.strategy.core.NotificationStrategy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class EmailNotificationStrategy
        implements NotificationStrategy {

    @Override
    public NotificationChannel supports() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public NotificationMessage execute(
            NotificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        if (request.subject().isBlank()) {
            throw new IllegalArgumentException(
                    "Email subject must not be blank"
            );
        }

        String html = """
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

        Map<String, String> metadata =
                new LinkedHashMap<>(
                        request.attributes()
                );

        metadata.put(
                "strategy",
                "email"
        );

        return new NotificationMessage(
                supports(),
                request.recipient(),
                request.subject(),
                html,
                "text/html",
                metadata
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
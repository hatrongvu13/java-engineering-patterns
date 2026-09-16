package com.htv.patterns.behavioral.templatemethod.email;

import com.htv.patterns.behavioral.templatemethod.core.AbstractDocumentGenerator;
import com.htv.patterns.behavioral.templatemethod.core.DocumentData;
import com.htv.patterns.behavioral.templatemethod.core.DocumentFormat;
import com.htv.patterns.behavioral.templatemethod.core.DocumentRequest;
import com.htv.patterns.behavioral.templatemethod.core.DocumentStorage;
import com.htv.patterns.behavioral.templatemethod.core.FileNameSanitizer;
import com.htv.patterns.behavioral.templatemethod.core.RenderedDocument;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public final class EmailTemplateGenerator
        extends AbstractDocumentGenerator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
            );

    public EmailTemplateGenerator(
            DocumentStorage storage
    ) {
        super(storage);
    }

    @Override
    protected void validate(
            DocumentRequest request
    ) {
        super.validate(request);

        if (
                !EMAIL_PATTERN
                        .matcher(request.recipient())
                        .matches()
        ) {
            throw new IllegalArgumentException(
                    "Invalid email recipient: "
                            + request.recipient()
            );
        }
    }

    @Override
    protected DocumentData loadData(
            DocumentRequest request
    ) {
        Map<String, Object> values =
                new LinkedHashMap<>(
                        request.parameters()
                );

        values.put(
                "recipient",
                request.recipient()
        );

        return new DocumentData(
                request.templateCode(),
                values,
                Instant.now()
        );
    }

    @Override
    protected RenderedDocument render(
            DocumentRequest request,
            DocumentData data
    ) {
        String title = textValue(
                data,
                "title",
                request.outputName()
        );

        String message = textValue(
                data,
                "message",
                ""
        );

        String html = """
            <!DOCTYPE html>
            <html lang="%s">
              <head>
                <meta charset="UTF-8">
                <title>%s</title>
              </head>
              <body>
                <h1>%s</h1>
                <p>%s</p>
              </body>
            </html>
            """.formatted(
                escapeHtml(request.language()),
                escapeHtml(title),
                escapeHtml(title),
                escapeHtml(message)
        );

        return new RenderedDocument(
                format(),
                FileNameSanitizer.withExtension(
                        request.outputName(),
                        format()
                ),
                html.getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }

    @Override
    protected DocumentFormat format() {
        return DocumentFormat.HTML;
    }

    private static String textValue(
            DocumentData data,
            String key,
            String defaultValue
    ) {
        Object value = data.values().get(key);

        if (value == null) {
            return defaultValue;
        }

        return Objects.toString(
                value,
                defaultValue
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
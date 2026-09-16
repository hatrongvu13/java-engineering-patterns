package com.htv.patterns.behavioral.templatemethod.email;

import com.htv.patterns.behavioral.templatemethod.core.DocumentFormat;
import com.htv.patterns.behavioral.templatemethod.core.DocumentRequest;
import com.htv.patterns.behavioral.templatemethod.core.DocumentResult;
import com.htv.patterns.behavioral.templatemethod.storage.InMemoryDocumentStorage;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTemplateGeneratorTest {

    @Test
    void shouldGenerateValidHtmlDocument() {
        InMemoryDocumentStorage storage =
                new InMemoryDocumentStorage();

        EmailTemplateGenerator generator =
                new EmailTemplateGenerator(storage);

        DocumentResult result =
                generator.generate(
                        new DocumentRequest(
                                "welcome-email",
                                "Thư chào mừng",
                                "developer@example.com",
                                "vi",
                                Map.of(
                                        "title",
                                        "Chào mừng",
                                        "message",
                                        "Chào mừng bạn đến với hệ thống"
                                )
                        )
                );

        assertThat(result.format())
                .isEqualTo(
                        DocumentFormat.HTML
                );

        assertThat(result.fileName())
                .isEqualTo(
                        "thu-chao-mung.html"
                );

        String html = new String(
                storage.find(
                        result.location()
                ).orElseThrow(),
                StandardCharsets.UTF_8
        );

        assertThat(html)
                .contains("<!DOCTYPE html>")
                .contains("<html lang=\"vi\">")
                .contains(
                        "<h1>Chào mừng</h1>"
                );
    }

    @Test
    void shouldEscapeUnsafeHtml() {
        InMemoryDocumentStorage storage =
                new InMemoryDocumentStorage();

        EmailTemplateGenerator generator =
                new EmailTemplateGenerator(storage);

        DocumentResult result =
                generator.generate(
                        new DocumentRequest(
                                "security-email",
                                "security-email",
                                "developer@example.com",
                                "en",
                                Map.of(
                                        "title",
                                        "<Admin>",
                                        "message",
                                        "<script>alert('x')</script>"
                                )
                        )
                );

        String html = new String(
                storage.find(
                        result.location()
                ).orElseThrow(),
                StandardCharsets.UTF_8
        );

        assertThat(html)
                .doesNotContain("<script>")
                .contains("&lt;script&gt;")
                .contains("&lt;Admin&gt;");
    }

    @Test
    void shouldRejectInvalidRecipient() {
        EmailTemplateGenerator generator =
                new EmailTemplateGenerator(
                        new InMemoryDocumentStorage()
                );

        DocumentRequest request =
                new DocumentRequest(
                        "welcome-email",
                        "welcome-email",
                        "invalid-email",
                        "vi",
                        Map.of(
                                "title",
                                "Welcome"
                        )
                );

        assertThatThrownBy(
                () -> generator.generate(request)
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessageContaining(
                        "Invalid email recipient"
                );
    }
}
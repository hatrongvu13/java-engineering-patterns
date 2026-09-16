package com.htv.patterns.behavioral.templatemethod.word;

import com.htv.patterns.behavioral.templatemethod.core.DocumentFormat;
import com.htv.patterns.behavioral.templatemethod.core.DocumentRequest;
import com.htv.patterns.behavioral.templatemethod.core.DocumentResult;
import com.htv.patterns.behavioral.templatemethod.storage.InMemoryDocumentStorage;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class WordDocumentGeneratorTest {

    @Test
    void shouldGenerateValidWordDocument()
            throws IOException {

        InMemoryDocumentStorage storage =
                new InMemoryDocumentStorage();

        WordDocumentGenerator generator =
                new WordDocumentGenerator(storage);

        DocumentRequest request =
                new DocumentRequest(
                        "approval-result",
                        "Kết quả phê duyệt",
                        "",
                        "vi",
                        Map.of(
                                "customerName",
                                "Nguyễn Văn A",
                                "status",
                                "Approved"
                        )
                );

        DocumentResult result =
                generator.generate(request);

        assertThat(result.format())
                .isEqualTo(
                        DocumentFormat.WORD
                );

        assertThat(result.fileName())
                .isEqualTo(
                        "ket-qua-phe-duyet.docx"
                );

        byte[] content =
                storage.find(
                        result.location()
                ).orElseThrow();

        try (
                XWPFDocument document =
                        new XWPFDocument(
                                new ByteArrayInputStream(
                                        content
                                )
                        )
        ) {
            String paragraphs =
                    document.getParagraphs()
                            .stream()
                            .map(
                                    XWPFParagraph::getText
                            )
                            .collect(
                                    Collectors.joining(
                                            "\n"
                                    )
                            );

            assertThat(paragraphs)
                    .contains(
                            "Kết quả phê duyệt"
                    )
                    .contains(
                            "Template: approval-result"
                    )
                    .contains(
                            "Language: vi"
                    );

            assertThat(document.getTables())
                    .hasSize(1);

            XWPFTable table =
                    document.getTables().get(0);

            assertThat(table.getRows())
                    .hasSize(
                            1
                                    + request
                                    .parameters()
                                    .size()
                    );

            assertThat(
                    table.getRow(0)
                            .getCell(0)
                            .getText()
            ).contains("Field");

            assertThat(
                    table.getRow(0)
                            .getCell(1)
                            .getText()
            ).contains("Value");

            assertThat(
                    tableContainsEntry(
                            table,
                            "customerName",
                            "Nguyễn Văn A"
                    )
            ).isTrue();

            assertThat(
                    tableContainsEntry(
                            table,
                            "status",
                            "Approved"
                    )
            ).isTrue();

            assertThat(
                    tableContainsKey(
                            table,
                            "language"
                    )
            ).isFalse();

            assertThat(
                    tableContainsKey(
                            table,
                            "outputName"
                    )
            ).isFalse();
        }
    }

    private static boolean tableContainsEntry(
            XWPFTable table,
            String expectedKey,
            String expectedValue
    ) {
        return table.getRows()
                .stream()
                .skip(1)
                .anyMatch(row ->
                        row.getTableCells().size() >= 2
                                && row.getCell(0)
                                .getText()
                                .contains(expectedKey)
                                && row.getCell(1)
                                .getText()
                                .contains(expectedValue)
                );
    }

    private static boolean tableContainsKey(
            XWPFTable table,
            String expectedKey
    ) {
        return table.getRows()
                .stream()
                .skip(1)
                .anyMatch(row ->
                        !row.getTableCells().isEmpty()
                                && row.getCell(0)
                                .getText()
                                .contains(expectedKey)
                );
    }
}
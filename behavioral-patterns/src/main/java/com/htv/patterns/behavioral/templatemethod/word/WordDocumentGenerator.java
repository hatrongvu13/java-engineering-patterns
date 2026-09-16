package com.htv.patterns.behavioral.templatemethod.word;

import com.htv.patterns.behavioral.templatemethod.core.AbstractDocumentGenerator;
import com.htv.patterns.behavioral.templatemethod.core.DocumentData;
import com.htv.patterns.behavioral.templatemethod.core.DocumentFormat;
import com.htv.patterns.behavioral.templatemethod.core.DocumentGenerationException;
import com.htv.patterns.behavioral.templatemethod.core.DocumentRequest;
import com.htv.patterns.behavioral.templatemethod.core.DocumentStorage;
import com.htv.patterns.behavioral.templatemethod.core.FileNameSanitizer;
import com.htv.patterns.behavioral.templatemethod.core.RenderedDocument;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;

public final class WordDocumentGenerator
        extends AbstractDocumentGenerator {

    private static final String LANGUAGE_KEY =
            "language";

    private static final String OUTPUT_NAME_KEY =
            "outputName";

    public WordDocumentGenerator(
            DocumentStorage storage
    ) {
        super(storage);
    }

    @Override
    protected DocumentData loadData(
            DocumentRequest request
    ) {
        return new DocumentData(
                request.templateCode(),
                request.parameters(),
                Instant.now()
        );
    }

    @Override
    protected RenderedDocument render(
            DocumentRequest request,
            DocumentData data
    ) {
        try (
                XWPFDocument document =
                        new XWPFDocument();

                ByteArrayOutputStream output =
                        new ByteArrayOutputStream()
        ) {
            createTitle(
                    document,
                    request.outputName()
            );

            createMetadata(
                    document,
                    request
            );

            createDataTable(
                    document,
                    data.values()
            );

            document.write(output);

            return new RenderedDocument(
                    format(),
                    FileNameSanitizer.withExtension(
                            request.outputName(),
                            format()
                    ),
                    output.toByteArray()
            );
        } catch (IOException exception) {
            throw new DocumentGenerationException(
                    "Unable to generate Word document",
                    exception
            );
        }
    }

    @Override
    protected DocumentFormat format() {
        return DocumentFormat.WORD;
    }

    private static void createTitle(
            XWPFDocument document,
            String title
    ) {
        XWPFParagraph paragraph =
                document.createParagraph();

        paragraph.setAlignment(
                ParagraphAlignment.CENTER
        );

        XWPFRun run =
                paragraph.createRun();

        run.setText(title);
        run.setBold(true);
        run.setFontSize(18);
    }

    private static void createMetadata(
            XWPFDocument document,
            DocumentRequest request
    ) {
        XWPFParagraph paragraph =
                document.createParagraph();

        XWPFRun run =
                paragraph.createRun();

        run.setText(
                "Template: "
                        + request.templateCode()
        );

        run.addBreak();

        run.setText(
                "Language: "
                        + request.language()
        );
    }

    private static void createDataTable(
            XWPFDocument document,
            Map<String, Object> values
    ) {
        XWPFTable table =
                document.createTable();

        XWPFTableRow header =
                table.getRow(0);

        setCellText(
                header.getCell(0),
                "Field",
                true
        );

        XWPFTableCell valueHeader =
                header.addNewTableCell();

        setCellText(
                valueHeader,
                "Value",
                true
        );

        values.entrySet()
                .stream()
                .filter(
                        entry ->
                                isBusinessField(
                                        entry.getKey()
                                )
                )
                .forEach(entry -> {
                    XWPFTableRow row =
                            table.createRow();

                    setCellText(
                            row.getCell(0),
                            entry.getKey(),
                            false
                    );

                    setCellText(
                            row.getCell(1),
                            toDisplayValue(
                                    entry.getValue()
                            ),
                            false
                    );
                });
    }

    private static boolean isBusinessField(
            String key
    ) {
        return !LANGUAGE_KEY.equals(key)
                && !OUTPUT_NAME_KEY.equals(key);
    }

    private static String toDisplayValue(
            Object value
    ) {
        return value == null
                ? ""
                : String.valueOf(value);
    }

    private static void setCellText(
            XWPFTableCell cell,
            String value,
            boolean bold
    ) {
        XWPFParagraph paragraph;

        if (cell.getParagraphs().isEmpty()) {
            paragraph =
                    cell.addParagraph();
        } else {
            paragraph =
                    cell.getParagraphs().get(0);

            clearRuns(paragraph);
        }

        XWPFRun run =
                paragraph.createRun();

        run.setBold(bold);
        run.setText(value);
    }

    private static void clearRuns(
            XWPFParagraph paragraph
    ) {
        for (
                int index =
                paragraph.getRuns().size() - 1;
                index >= 0;
                index--
        ) {
            paragraph.removeRun(index);
        }
    }
}
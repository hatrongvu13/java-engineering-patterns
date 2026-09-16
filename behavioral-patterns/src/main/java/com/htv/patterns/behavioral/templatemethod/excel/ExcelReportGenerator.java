package com.htv.patterns.behavioral.templatemethod.excel;

import com.htv.patterns.behavioral.templatemethod.core.AbstractDocumentGenerator;
import com.htv.patterns.behavioral.templatemethod.core.DocumentData;
import com.htv.patterns.behavioral.templatemethod.core.DocumentFormat;
import com.htv.patterns.behavioral.templatemethod.core.DocumentGenerationException;
import com.htv.patterns.behavioral.templatemethod.core.DocumentRequest;
import com.htv.patterns.behavioral.templatemethod.core.DocumentStorage;
import com.htv.patterns.behavioral.templatemethod.core.FileNameSanitizer;
import com.htv.patterns.behavioral.templatemethod.core.RenderedDocument;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;

public final class ExcelReportGenerator
        extends AbstractDocumentGenerator {

    public ExcelReportGenerator(
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
                XSSFWorkbook workbook =
                        new XSSFWorkbook();

                ByteArrayOutputStream output =
                        new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.createSheet(
                    safeSheetName(
                            request.outputName()
                    )
            );

            CellStyle headerStyle =
                    createHeaderStyle(workbook);

            createTitleRow(
                    sheet,
                    request.outputName(),
                    headerStyle
            );

            createHeaderRow(
                    sheet,
                    headerStyle
            );

            int rowIndex = 2;

            for (
                    Map.Entry<String, Object> entry
                    : data.values().entrySet()
            ) {
                Row row = sheet.createRow(
                        rowIndex++
                );

                row.createCell(0)
                        .setCellValue(
                                entry.getKey()
                        );

                writeCellValue(
                        row.createCell(1),
                        entry.getValue()
                );
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.createFreezePane(0, 2);

            workbook.write(output);

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
                    "Unable to generate Excel document",
                    exception
            );
        }
    }

    @Override
    protected DocumentFormat format() {
        return DocumentFormat.EXCEL;
    }

    private static CellStyle createHeaderStyle(
            XSSFWorkbook workbook
    ) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(
                IndexedColors.WHITE.getIndex()
        );

        CellStyle style =
                workbook.createCellStyle();

        style.setFont(font);
        style.setFillForegroundColor(
                IndexedColors.DARK_BLUE.getIndex()
        );
        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        return style;
    }

    private static void createTitleRow(
            Sheet sheet,
            String title,
            CellStyle headerStyle
    ) {
        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(headerStyle);
    }

    private static void createHeaderRow(
            Sheet sheet,
            CellStyle headerStyle
    ) {
        Row row = sheet.createRow(1);

        Cell keyCell = row.createCell(0);
        keyCell.setCellValue("Field");
        keyCell.setCellStyle(headerStyle);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue("Value");
        valueCell.setCellStyle(headerStyle);
    }

    private static void writeCellValue(
            Cell cell,
            Object value
    ) {
        if (value == null) {
            cell.setBlank();
            return;
        }

        if (value instanceof Number number) {
            cell.setCellValue(
                    number.doubleValue()
            );
            return;
        }

        if (value instanceof Boolean booleanValue) {
            cell.setCellValue(booleanValue);
            return;
        }

        cell.setCellValue(
                String.valueOf(value)
        );
    }

    private static String safeSheetName(
            String value
    ) {
        String sanitized = value
                .replaceAll(
                        "[:\\\\/?*\\[\\]]",
                        "-"
                )
                .trim();

        if (sanitized.isEmpty()) {
            return "Report";
        }

        if (sanitized.length() > 31) {
            return sanitized.substring(0, 31);
        }

        return sanitized;
    }
}
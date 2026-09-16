package com.htv.patterns.behavioral.templatemethod.excel;

import com.htv.patterns.behavioral.templatemethod.core.DocumentFormat;
import com.htv.patterns.behavioral.templatemethod.core.DocumentRequest;
import com.htv.patterns.behavioral.templatemethod.core.DocumentResult;
import com.htv.patterns.behavioral.templatemethod.storage.InMemoryDocumentStorage;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExcelReportGeneratorTest {

    @Test
    void shouldGenerateValidExcelWorkbook()
            throws IOException {

        InMemoryDocumentStorage storage =
                new InMemoryDocumentStorage();

        ExcelReportGenerator generator =
                new ExcelReportGenerator(storage);

        DocumentResult result =
                generator.generate(
                        new DocumentRequest(
                                "monthly-finance",
                                "Báo cáo tài chính tháng 01",
                                Map.of(
                                        "branchCode",
                                        "020",
                                        "month",
                                        "2026-01",
                                        "total",
                                        1250000
                                )
                        )
                );

        assertThat(result.format())
                .isEqualTo(
                        DocumentFormat.EXCEL
                );

        assertThat(result.fileName())
                .isEqualTo(
                        "bao-cao-tai-chinh-thang-01.xlsx"
                );

        byte[] content =
                storage.find(
                        result.location()
                ).orElseThrow();

        try (
                XSSFWorkbook workbook =
                        new XSSFWorkbook(
                                new ByteArrayInputStream(
                                        content
                                )
                        )
        ) {
            assertThat(
                    workbook.getNumberOfSheets()
            ).isEqualTo(1);

            Sheet sheet =
                    workbook.getSheetAt(0);

            assertThat(sheet)
                    .isNotNull();

            Row header = sheet.getRow(1);

            assertThat(
                    header.getCell(0)
                            .getStringCellValue()
            ).isEqualTo("Field");

            assertThat(
                    header.getCell(1)
                            .getStringCellValue()
            ).isEqualTo("Value");

            assertThat(
                    sheet.getLastRowNum()
            ).isGreaterThanOrEqualTo(4);
        }
    }

    @Test
    void shouldCreateDifferentLocationsForSameName() {
        InMemoryDocumentStorage storage =
                new InMemoryDocumentStorage();

        ExcelReportGenerator generator =
                new ExcelReportGenerator(storage);

        DocumentRequest request =
                new DocumentRequest(
                        "monthly-finance",
                        "monthly-finance",
                        Map.of(
                                "month",
                                "2026-01"
                        )
                );

        DocumentResult first =
                generator.generate(request);

        DocumentResult second =
                generator.generate(request);

        assertThat(first.location())
                .isNotEqualTo(
                        second.location()
                );

        assertThat(storage.size())
                .isEqualTo(2);
    }
}
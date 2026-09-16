package com.htv.patterns.creational.builder.fluent;

import com.htv.patterns.creational.builder.domain.ReportFormat;
import com.htv.patterns.creational.builder.domain.ReportRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FluentReportRequestTest {

    @Test
    void shouldBuildReportUsingBusinessLanguage() {
        ReportRequest request =
                FluentReportRequest
                        .report("monthly-finance")
                        .asExcel()
                        .between(
                                LocalDate.of(
                                        2026,
                                        1,
                                        1
                                ),
                                LocalDate.of(
                                        2026,
                                        1,
                                        31
                                )
                        )
                        .inEnglish()
                        .withSummary()
                        .withDetails()
                        .withChart()
                        .groupedBy("BRANCH")
                        .withParameter(
                                "branchCode",
                                "020"
                        )
                        .build();

        assertThat(request.format())
                .isEqualTo(
                        ReportFormat.EXCEL
                );

        assertThat(request.language())
                .isEqualTo("en");

        assertThat(request.includeSummary())
                .isTrue();

        assertThat(request.includeDetails())
                .isTrue();

        assertThat(request.includeChart())
                .isTrue();
    }

    @Test
    void shouldBuildDailyReport() {
        LocalDate reportDate =
                LocalDate.of(
                        2026,
                        3,
                        10
                );

        ReportRequest request =
                FluentReportRequest
                        .report("daily-report")
                        .asPdf()
                        .forDate(reportDate)
                        .withSummary()
                        .build();

        assertThat(request.fromDate())
                .isEqualTo(reportDate);

        assertThat(request.toDate())
                .isEqualTo(reportDate);
    }

    @Test
    void shouldRejectReportWithoutContentSelection() {
        assertThatThrownBy(
                () -> FluentReportRequest
                        .report("empty-report")
                        .asPdf()
                        .forDate(
                                LocalDate.now()
                        )
                        .build()
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessageContaining(
                        "At least one report content option"
                );
    }
}
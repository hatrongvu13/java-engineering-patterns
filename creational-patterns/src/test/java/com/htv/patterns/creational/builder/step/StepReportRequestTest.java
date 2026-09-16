package com.htv.patterns.creational.builder.step;

import com.htv.patterns.creational.builder.domain.ReportFormat;
import com.htv.patterns.creational.builder.domain.ReportRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class StepReportRequestTest {

    @Test
    void shouldBuildRequestUsingRequiredSteps() {
        ReportRequest request =
                StepReportRequest.builder()
                        .reportCode(
                                "monthly-finance"
                        )
                        .format(
                                ReportFormat.EXCEL
                        )
                        .dateRange(
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
                        .language("en")
                        .includeChart()
                        .groupBy("BRANCH")
                        .parameter("branchCode", "020")
                        .build();

        assertThat(request.reportCode()).isEqualTo("monthly-finance");

        assertThat(request.format()).isEqualTo(ReportFormat.EXCEL);

        assertThat(request.language())
                .isEqualTo("en");

        assertThat(request.includeChart())
                .isTrue();
    }

    @Test
    void shouldBuildReportForSingleDate() {
        LocalDate date = LocalDate.of(2026, 4, 15);

        ReportRequest request = StepReportRequest.builder()
                .reportCode("daily-report")
                .format(ReportFormat.PDF)
                .forDate(date)
                .build();

        assertThat(request.fromDate())
                .isEqualTo(date);

        assertThat(request.toDate())
                .isEqualTo(date);
    }
}
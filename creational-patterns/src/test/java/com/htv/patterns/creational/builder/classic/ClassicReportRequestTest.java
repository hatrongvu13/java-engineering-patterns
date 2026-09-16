package com.htv.patterns.creational.builder.classic;

import com.htv.patterns.creational.builder.domain.ReportFormat;
import com.htv.patterns.creational.builder.domain.ReportRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClassicReportRequestTest {

    @Test
    void shouldBuildCompleteReportRequest() {
        ReportRequest request =
                ClassicReportRequest.builder()
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
                        .language("vi")
                        .includeSummary(true)
                        .includeDetails(true)
                        .includeChart(true)
                        .groupBy("BRANCH")
                        .groupBy("PRODUCT")
                        .parameter(
                                "branchCode",
                                "020"
                        )
                        .build();

        assertThat(request.reportCode())
                .isEqualTo(
                        "monthly-finance"
                );

        assertThat(request.format())
                .isEqualTo(
                        ReportFormat.EXCEL
                );

        assertThat(request.language())
                .isEqualTo("vi");

        assertThat(request.groupBy())
                .containsExactly(
                        "BRANCH",
                        "PRODUCT"
                );

        assertThat(request.parameters())
                .containsEntry(
                        "branchCode",
                        "020"
                );
    }

    @Test
    void shouldUseDefaultConfiguration() {
        ReportRequest request =
                ClassicReportRequest.builder()
                        .reportCode("daily-report")
                        .format(ReportFormat.PDF)
                        .dateRange(
                                LocalDate.of(
                                        2026,
                                        1,
                                        1
                                ),
                                LocalDate.of(
                                        2026,
                                        1,
                                        1
                                )
                        )
                        .build();

        assertThat(request.language())
                .isEqualTo("vi");

        assertThat(request.includeSummary())
                .isTrue();

        assertThat(request.includeDetails())
                .isTrue();

        assertThat(request.includeChart())
                .isFalse();
    }

    @Test
    void shouldRejectMissingReportCode() {
        assertThatThrownBy(
                () -> ClassicReportRequest
                        .builder()
                        .format(ReportFormat.PDF)
                        .dateRange(
                                LocalDate.now(),
                                LocalDate.now()
                        )
                        .build()
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "reportCode must not be null"
                );
    }

    @Test
    void shouldRejectInvalidDateRange() {
        assertThatThrownBy(
                () -> ClassicReportRequest
                        .builder()
                        .reportCode("report")
                        .format(ReportFormat.PDF)
                        .dateRange(
                                LocalDate.of(
                                        2026,
                                        2,
                                        1
                                ),
                                LocalDate.of(
                                        2026,
                                        1,
                                        1
                                )
                        )
                        .build()
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessage(
                        "fromDate must not be after toDate"
                );
    }

    @Test
    void shouldCreateDefensiveCopyOfGroups() {
        List<String> groups =
                new ArrayList<>();

        groups.add("BRANCH");

        ReportRequest request =
                ClassicReportRequest.builder()
                        .reportCode("report")
                        .format(ReportFormat.EXCEL)
                        .dateRange(
                                LocalDate.now(),
                                LocalDate.now()
                        )
                        .groupBy(groups)
                        .build();

        groups.add("PRODUCT");

        assertThat(request.groupBy())
                .containsExactly("BRANCH");

        assertThatThrownBy(
                () -> request.groupBy()
                        .add("REGION")
        )
                .isInstanceOf(
                        UnsupportedOperationException.class
                );
    }
}
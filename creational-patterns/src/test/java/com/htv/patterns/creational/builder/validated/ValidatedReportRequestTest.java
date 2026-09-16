package com.htv.patterns.creational.builder.validated;

import com.htv.patterns.creational.builder.domain.ReportFormat;
import com.htv.patterns.creational.builder.domain.ReportRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidatedReportRequestTest {

    @Test
    void shouldBuildValidRequest() {
        ReportRequest request =
                ValidatedReportRequest.builder()
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
                        .language("VI")
                        .includeSummary()
                        .groupBy("BRANCH")
                        .groupBy("BRANCH")
                        .build();

        assertThat(request.language())
                .isEqualTo("vi");

        assertThat(request.groupBy())
                .containsExactly("BRANCH");
    }

    @Test
    void shouldReturnAllValidationViolations() {
        List<String> violations =
                ValidatedReportRequest.builder()
                        .language("fr")
                        .validate();

        assertThat(violations)
                .contains(
                        "reportCode must not be blank",
                        "format must not be null",
                        "fromDate must not be null",
                        "toDate must not be null",
                        "Unsupported report language: fr",
                        "At least one report content "
                                + "option must be enabled"
                );
    }

    @Test
    void shouldThrowExceptionContainingAllViolations() {
        assertThatThrownBy(
                () -> ValidatedReportRequest
                        .builder()
                        .language("fr")
                        .build()
        )
                .isInstanceOf(
                        ReportValidationException.class
                )
                .satisfies(
                        throwable -> {
                            ReportValidationException exception =
                                    (ReportValidationException) throwable;

                            assertThat(
                                    exception.violations()
                            )
                                    .contains(
                                            "reportCode must not be blank",
                                            "format must not be null",
                                            "fromDate must not be null",
                                            "toDate must not be null",
                                            "Unsupported report language: fr"
                                    );
                        }
                );
    }

    @Test
    void shouldRejectInvalidDateRange() {
        List<String> violations =
                ValidatedReportRequest.builder()
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
                        .includeSummary()
                        .validate();

        assertThat(violations)
                .contains(
                        "fromDate must not be after toDate"
                );
    }

    @Test
    void shouldExposeImmutableViolationList() {
        ReportValidationException exception =
                new ReportValidationException(
                        List.of(
                                "invalid report"
                        )
                );

        assertThatThrownBy(
                () -> exception
                        .violations()
                        .add("another violation")
        )
                .isInstanceOf(
                        UnsupportedOperationException.class
                );
    }
}
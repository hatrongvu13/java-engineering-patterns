package com.htv.patterns.creational.builder.domain;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

public final class ReportRequestValidator {

    private static final long MAXIMUM_REPORT_RANGE_DAYS =
            366L;

    private static final Set<String>
            SUPPORTED_LANGUAGES =
            Set.of("vi", "en");

    private ReportRequestValidator() {
        throw new AssertionError(
                "Validator class must not be instantiated"
        );
    }

    public static void validate(
            ReportRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        validateDateRange(request);
        validateLanguage(request);
        validateContentSelection(request);
    }

    private static void validateDateRange(
            ReportRequest request
    ) {
        if (
                request.fromDate()
                        .isAfter(request.toDate())
        ) {
            throw new IllegalArgumentException(
                    "fromDate must not be after toDate"
            );
        }

        long rangeDays = ChronoUnit.DAYS.between(
                request.fromDate(),
                request.toDate()
        );

        if (rangeDays > MAXIMUM_REPORT_RANGE_DAYS) {
            throw new IllegalArgumentException(
                    "Report date range must not exceed "
                            + MAXIMUM_REPORT_RANGE_DAYS
                            + " days"
            );
        }
    }

    private static void validateLanguage(
            ReportRequest request
    ) {
        if (
                !SUPPORTED_LANGUAGES.contains(
                        request.language()
                )
        ) {
            throw new IllegalArgumentException(
                    "Unsupported report language: "
                            + request.language()
            );
        }
    }

    private static void validateContentSelection(
            ReportRequest request
    ) {
        boolean hasSelectedContent =
                request.includeSummary()
                        || request.includeDetails()
                        || request.includeChart();

        if (!hasSelectedContent) {
            throw new IllegalArgumentException(
                    "At least one report content option "
                            + "must be enabled"
            );
        }
    }
}
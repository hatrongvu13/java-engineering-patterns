package com.htv.patterns.creational.builder.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record ReportRequest(
        String reportCode,
        ReportFormat format,
        LocalDate fromDate,
        LocalDate toDate,
        String language,
        boolean includeSummary,
        boolean includeDetails,
        boolean includeChart,
        List<String> groupBy,
        Map<String, String> parameters
) {

    public ReportRequest {
        reportCode = requireText(
                reportCode,
                "reportCode"
        );

        Objects.requireNonNull(
                format,
                "format must not be null"
        );

        Objects.requireNonNull(
                fromDate,
                "fromDate must not be null"
        );

        Objects.requireNonNull(
                toDate,
                "toDate must not be null"
        );

        language = normalizeLanguage(language);

        groupBy = groupBy == null
                ? List.of()
                : List.copyOf(groupBy);

        parameters = parameters == null
                ? Map.of()
                : Map.copyOf(parameters);
    }

    private static String requireText(
            String value,
            String fieldName
    ) {
        Objects.requireNonNull(
                value,
                fieldName + " must not be null"
        );

        String normalized = value.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(
                    fieldName + " must not be blank"
            );
        }

        return normalized;
    }

    private static String normalizeLanguage(
            String language
    ) {
        if (language == null || language.isBlank()) {
            return "vi";
        }

        return language.trim().toLowerCase();
    }
}
package com.htv.patterns.creational.builder.validated;

import com.htv.patterns.creational.builder.domain.ReportFormat;
import com.htv.patterns.creational.builder.domain.ReportRequest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ValidatedReportRequest {

    private ValidatedReportRequest() {
        throw new AssertionError(
                "Factory class must not be instantiated"
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private static final long
                MAXIMUM_REPORT_RANGE_DAYS = 366L;

        private static final Set<String>
                SUPPORTED_LANGUAGES =
                Set.of("vi", "en");

        private String reportCode;
        private ReportFormat format;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String language = "vi";

        private boolean includeSummary;
        private boolean includeDetails;
        private boolean includeChart;

        private final List<String> groupBy =
                new ArrayList<>();

        private final Map<String, String> parameters =
                new LinkedHashMap<>();

        public Builder reportCode(
                String reportCode
        ) {
            this.reportCode = reportCode;
            return this;
        }

        public Builder format(
                ReportFormat format
        ) {
            this.format = format;
            return this;
        }

        public Builder dateRange(
                LocalDate fromDate,
                LocalDate toDate
        ) {
            this.fromDate = fromDate;
            this.toDate = toDate;

            return this;
        }

        public Builder language(
                String language
        ) {
            this.language = language;
            return this;
        }

        public Builder includeSummary() {
            this.includeSummary = true;
            return this;
        }

        public Builder includeDetails() {
            this.includeDetails = true;
            return this;
        }

        public Builder includeChart() {
            this.includeChart = true;
            return this;
        }

        public Builder groupBy(
                String field
        ) {
            if (field != null) {
                groupBy.add(field);
            }

            return this;
        }

        public Builder parameter(
                String name,
                String value
        ) {
            if (name != null) {
                parameters.put(
                        name,
                        value
                );
            }

            return this;
        }

        public ReportRequest build() {
            List<String> violations =
                    validate();

            if (!violations.isEmpty()) {
                throw new ReportValidationException(
                        violations
                );
            }

            return new ReportRequest(
                    reportCode.trim(),
                    format,
                    fromDate,
                    toDate,
                    language.trim().toLowerCase(),
                    includeSummary,
                    includeDetails,
                    includeChart,
                    normalizeGroups(),
                    normalizeParameters()
            );
        }

        public List<String> validate() {
            List<String> violations =
                    new ArrayList<>();

            validateReportCode(violations);
            validateFormat(violations);
            validateDates(violations);
            validateLanguage(violations);
            validateContentSelection(violations);
            validateGroups(violations);
            validateParameters(violations);

            return List.copyOf(violations);
        }

        private void validateReportCode(
                List<String> violations
        ) {
            if (
                    reportCode == null
                            || reportCode.isBlank()
            ) {
                violations.add(
                        "reportCode must not be blank"
                );
            }
        }

        private void validateFormat(
                List<String> violations
        ) {
            if (format == null) {
                violations.add(
                        "format must not be null"
                );
            }
        }

        private void validateDates(
                List<String> violations
        ) {
            if (fromDate == null) {
                violations.add(
                        "fromDate must not be null"
                );
            }

            if (toDate == null) {
                violations.add(
                        "toDate must not be null"
                );
            }

            if (
                    fromDate == null
                            || toDate == null
            ) {
                return;
            }

            if (fromDate.isAfter(toDate)) {
                violations.add(
                        "fromDate must not be after toDate"
                );

                return;
            }

            long rangeDays =
                    ChronoUnit.DAYS.between(
                            fromDate,
                            toDate
                    );

            if (
                    rangeDays
                            > MAXIMUM_REPORT_RANGE_DAYS
            ) {
                violations.add(
                        "Report date range must not exceed "
                                + MAXIMUM_REPORT_RANGE_DAYS
                                + " days"
                );
            }
        }

        private void validateLanguage(
                List<String> violations
        ) {
            if (
                    language == null
                            || language.isBlank()
            ) {
                violations.add(
                        "language must not be blank"
                );

                return;
            }

            String normalized =
                    language.trim().toLowerCase();

            if (
                    !SUPPORTED_LANGUAGES.contains(
                            normalized
                    )
            ) {
                violations.add(
                        "Unsupported report language: "
                                + language
                );
            }
        }

        private void validateContentSelection(
                List<String> violations
        ) {
            if (
                    !includeSummary
                            && !includeDetails
                            && !includeChart
            ) {
                violations.add(
                        "At least one report content "
                                + "option must be enabled"
                );
            }
        }

        private void validateGroups(
                List<String> violations
        ) {
            boolean containsBlank =
                    groupBy.stream()
                            .anyMatch(
                                    field ->
                                            field == null
                                                    || field.isBlank()
                            );

            if (containsBlank) {
                violations.add(
                        "groupBy must not contain blank fields"
                );
            }
        }

        private void validateParameters(
                List<String> violations
        ) {
            parameters.forEach(
                    (name, value) -> {
                        if (
                                name == null
                                        || name.isBlank()
                        ) {
                            violations.add(
                                    "Parameter name must not be blank"
                            );
                        }

                        if (value == null) {
                            violations.add(
                                    "Parameter value must not be null"
                            );
                        }
                    }
            );
        }

        private List<String> normalizeGroups() {
            return groupBy.stream()
                    .map(String::trim)
                    .distinct()
                    .toList();
        }

        private Map<String, String>
        normalizeParameters() {
            Map<String, String> normalized =
                    new LinkedHashMap<>();

            parameters.forEach(
                    (name, value) ->
                            normalized.put(
                                    name.trim(),
                                    value
                            )
            );

            return normalized;
        }
    }
}
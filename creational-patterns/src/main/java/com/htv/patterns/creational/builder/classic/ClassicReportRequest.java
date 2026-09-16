package com.htv.patterns.creational.builder.classic;

import com.htv.patterns.creational.builder.domain.ReportFormat;
import com.htv.patterns.creational.builder.domain.ReportRequest;
import com.htv.patterns.creational.builder.domain.ReportRequestValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ClassicReportRequest {

    private ClassicReportRequest() {
        throw new AssertionError(
                "Factory class must not be instantiated"
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String reportCode;
        private ReportFormat format;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String language = "vi";

        private boolean includeSummary = true;
        private boolean includeDetails = true;
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

        public Builder fromDate(
                LocalDate fromDate
        ) {
            this.fromDate = fromDate;
            return this;
        }

        public Builder toDate(
                LocalDate toDate
        ) {
            this.toDate = toDate;
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

        public Builder includeSummary(
                boolean includeSummary
        ) {
            this.includeSummary =
                    includeSummary;

            return this;
        }

        public Builder includeDetails(
                boolean includeDetails
        ) {
            this.includeDetails =
                    includeDetails;

            return this;
        }

        public Builder includeChart(
                boolean includeChart
        ) {
            this.includeChart =
                    includeChart;

            return this;
        }

        public Builder groupBy(
                String field
        ) {
            if (field != null && !field.isBlank()) {
                groupBy.add(field.trim());
            }

            return this;
        }

        public Builder groupBy(
                List<String> fields
        ) {
            if (fields != null) {
                fields.forEach(this::groupBy);
            }

            return this;
        }

        public Builder parameter(
                String name,
                String value
        ) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException(
                        "Parameter name must not be blank"
                );
            }

            if (value == null) {
                throw new NullPointerException(
                        "Parameter value must not be null"
                );
            }

            parameters.put(
                    name.trim(),
                    value
            );

            return this;
        }

        public ReportRequest build() {
            ReportRequest request =
                    new ReportRequest(
                            reportCode,
                            format,
                            fromDate,
                            toDate,
                            language,
                            includeSummary,
                            includeDetails,
                            includeChart,
                            groupBy,
                            parameters
                    );

            ReportRequestValidator.validate(
                    request
            );

            return request;
        }
    }
}
package com.htv.patterns.creational.builder.fluent;

import com.htv.patterns.creational.builder.domain.ReportFormat;
import com.htv.patterns.creational.builder.domain.ReportRequest;
import com.htv.patterns.creational.builder.domain.ReportRequestValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class FluentReportRequest {

    private FluentReportRequest() {
        throw new AssertionError(
                "Factory class must not be instantiated"
        );
    }

    public static Builder report(
            String reportCode
    ) {
        return new Builder(reportCode);
    }

    public static final class Builder {

        private final String reportCode;

        private ReportFormat format =
                ReportFormat.PDF;

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

        private Builder(
                String reportCode
        ) {
            this.reportCode =
                    Objects.requireNonNull(
                            reportCode,
                            "reportCode must not be null"
                    );
        }

        public Builder asPdf() {
            this.format = ReportFormat.PDF;
            return this;
        }

        public Builder asExcel() {
            this.format = ReportFormat.EXCEL;
            return this;
        }

        public Builder asWord() {
            this.format = ReportFormat.WORD;
            return this;
        }

        public Builder asCsv() {
            this.format = ReportFormat.CSV;
            return this;
        }

        public Builder forDate(
                LocalDate date
        ) {
            this.fromDate = date;
            this.toDate = date;

            return this;
        }

        public Builder between(
                LocalDate fromDate,
                LocalDate toDate
        ) {
            this.fromDate = fromDate;
            this.toDate = toDate;

            return this;
        }

        public Builder inVietnamese() {
            this.language = "vi";
            return this;
        }

        public Builder inEnglish() {
            this.language = "en";
            return this;
        }

        public Builder withSummary() {
            this.includeSummary = true;
            return this;
        }

        public Builder withDetails() {
            this.includeDetails = true;
            return this;
        }

        public Builder withChart() {
            this.includeChart = true;
            return this;
        }

        public Builder groupedBy(
                String field
        ) {
            if (field != null && !field.isBlank()) {
                groupBy.add(field.trim());
            }

            return this;
        }

        public Builder withParameter(
                String name,
                String value
        ) {
            Objects.requireNonNull(
                    name,
                    "Parameter name must not be null"
            );

            Objects.requireNonNull(
                    value,
                    "Parameter value must not be null"
            );

            String normalizedName =
                    name.trim();

            if (normalizedName.isEmpty()) {
                throw new IllegalArgumentException(
                        "Parameter name must not be blank"
                );
            }

            parameters.put(
                    normalizedName,
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
package com.htv.patterns.creational.builder.step;

import com.htv.patterns.creational.builder.domain.ReportFormat;
import com.htv.patterns.creational.builder.domain.ReportRequest;
import com.htv.patterns.creational.builder.domain.ReportRequestValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class StepReportRequest {

    private StepReportRequest() {
        throw new AssertionError(
                "Factory class must not be instantiated"
        );
    }

    public static ReportCodeStep builder() {
        return new Steps();
    }

    public interface ReportCodeStep {

        FormatStep reportCode(
                String reportCode
        );
    }

    public interface FormatStep {

        DateRangeStep format(
                ReportFormat format
        );
    }

    public interface DateRangeStep {

        OptionalStep dateRange(
                LocalDate fromDate,
                LocalDate toDate
        );

        OptionalStep forDate(
                LocalDate date
        );
    }

    public interface OptionalStep {

        OptionalStep language(
                String language
        );

        OptionalStep includeSummary();

        OptionalStep includeDetails();

        OptionalStep includeChart();

        OptionalStep groupBy(
                String field
        );

        OptionalStep parameter(
                String name,
                String value
        );

        ReportRequest build();
    }

    private static final class Steps
            implements
            ReportCodeStep,
            FormatStep,
            DateRangeStep,
            OptionalStep {

        private String reportCode;
        private ReportFormat format;
        private LocalDate fromDate;
        private LocalDate toDate;

        private String language = "vi";

        private boolean includeSummary =
                true;

        private boolean includeDetails =
                true;

        private boolean includeChart;

        private final List<String> groupBy =
                new ArrayList<>();

        private final Map<String, String> parameters =
                new LinkedHashMap<>();

        @Override
        public FormatStep reportCode(
                String reportCode
        ) {
            this.reportCode = reportCode;
            return this;
        }

        @Override
        public DateRangeStep format(
                ReportFormat format
        ) {
            this.format = format;
            return this;
        }

        @Override
        public OptionalStep dateRange(
                LocalDate fromDate,
                LocalDate toDate
        ) {
            this.fromDate = fromDate;
            this.toDate = toDate;

            return this;
        }

        @Override
        public OptionalStep forDate(
                LocalDate date
        ) {
            this.fromDate = date;
            this.toDate = date;

            return this;
        }

        @Override
        public OptionalStep language(
                String language
        ) {
            this.language = language;
            return this;
        }

        @Override
        public OptionalStep includeSummary() {
            this.includeSummary = true;
            return this;
        }

        @Override
        public OptionalStep includeDetails() {
            this.includeDetails = true;
            return this;
        }

        @Override
        public OptionalStep includeChart() {
            this.includeChart = true;
            return this;
        }

        @Override
        public OptionalStep groupBy(
                String field
        ) {
            if (field != null && !field.isBlank()) {
                groupBy.add(field.trim());
            }

            return this;
        }

        @Override
        public OptionalStep parameter(
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

        @Override
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
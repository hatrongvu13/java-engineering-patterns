package com.htv.patterns.creational.prototype.registry;

import com.htv.patterns.creational.prototype.domain.ReportSection;
import com.htv.patterns.creational.prototype.domain.ReportStyle;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DefaultReportTemplateRegistry {

    public static final String
            MONTHLY_FINANCE =
            "monthly-finance";

    public static final String
            DAILY_OPERATION =
            "daily-operation";

    private DefaultReportTemplateRegistry() {
        throw new AssertionError(
                "Factory class must not be instantiated"
        );
    }

    public static ReportTemplateRegistry create() {
        ReportTemplateRegistry registry =
                new ReportTemplateRegistry();

        registry.register(
                MONTHLY_FINANCE,
                monthlyFinanceTemplate()
        );

        registry.register(
                DAILY_OPERATION,
                dailyOperationTemplate()
        );

        return registry;
    }

    private static ReportTemplatePrototype
    monthlyFinanceTemplate() {
        List<ReportSection> sections =
                List.of(
                        new ReportSection(
                                "summary",
                                "Executive Summary",
                                true
                        ),
                        new ReportSection(
                                "revenue",
                                "Revenue Analysis",
                                true
                        ),
                        new ReportSection(
                                "expenses",
                                "Expense Analysis",
                                true
                        )
                );

        Map<String, String> parameters =
                new LinkedHashMap<>();

        parameters.put(
                "currency",
                "VND"
        );

        return new ReportTemplatePrototype(
                "monthly-finance",
                "Monthly Finance Report",
                "vi",
                sections,
                parameters,
                new ReportStyle(
                        "Arial",
                        "#2563EB",
                        11
                )
        );
    }

    private static ReportTemplatePrototype
    dailyOperationTemplate() {
        return new ReportTemplatePrototype(
                "daily-operation",
                "Daily Operation Report",
                "vi",
                List.of(
                        new ReportSection(
                                "operation-summary",
                                "Operation Summary",
                                true
                        ),
                        new ReportSection(
                                "incidents",
                                "Incidents",
                                true
                        )
                ),
                Map.of(
                        "timezone",
                        "Asia/Ho_Chi_Minh"
                ),
                new ReportStyle(
                        "Roboto",
                        "#15803D",
                        10
                )
        );
    }
}
package com.htv.patterns.creational.prototype.registry;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultReportTemplateRegistryTest {

    @Test
    void shouldRegisterDefaultTemplates() {
        ReportTemplateRegistry registry =
                DefaultReportTemplateRegistry
                        .create();

        assertThat(registry.size())
                .isEqualTo(2);

        assertThat(
                registry.registeredKeys()
        ).containsExactlyInAnyOrder(
                DefaultReportTemplateRegistry
                        .MONTHLY_FINANCE,
                DefaultReportTemplateRegistry
                        .DAILY_OPERATION
        );
    }

    @Test
    void shouldCreateCustomizedTemplateWithoutChangingDefault() {
        ReportTemplateRegistry registry =
                DefaultReportTemplateRegistry
                        .create();

        ReportTemplatePrototype customized =
                registry.create(
                        DefaultReportTemplateRegistry
                                .MONTHLY_FINANCE
                );

        customized.setLanguage("en");

        customized.getParameters()
                .put(
                        "branchCode",
                        "020"
                );

        ReportTemplatePrototype original =
                registry.create(
                        DefaultReportTemplateRegistry
                                .MONTHLY_FINANCE
                );

        assertThat(
                original.getLanguage()
        ).isEqualTo("vi");

        assertThat(
                original.getParameters()
        ).doesNotContainKey(
                "branchCode"
        );
    }
}
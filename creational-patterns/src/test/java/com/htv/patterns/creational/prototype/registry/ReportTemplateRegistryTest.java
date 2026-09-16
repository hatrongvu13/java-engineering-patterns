package com.htv.patterns.creational.prototype.registry;

import com.htv.patterns.creational.prototype.domain.ReportSection;
import com.htv.patterns.creational.prototype.domain.ReportStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReportTemplateRegistryTest {

    private ReportTemplateRegistry registry;

    @BeforeEach
    void setUp() {
        registry =
                new ReportTemplateRegistry();
    }

    @Test
    void shouldRegisterAndCreatePrototype() {
        registry.register(
                "monthly-report",
                createPrototype()
        );

        ReportTemplatePrototype result =
                registry.create(
                        "monthly-report"
                );

        assertThat(result.getCode())
                .isEqualTo(
                        "monthly-report"
                );

        assertThat(registry.size())
                .isEqualTo(1);
    }

    @Test
    void shouldNormalizeRegistryKey() {
        registry.register(
                " Monthly-Report ",
                createPrototype()
        );

        assertThat(
                registry.contains(
                        "monthly-report"
                )
        ).isTrue();

        assertThat(
                registry.create(
                        " MONTHLY-REPORT "
                )
        ).isNotNull();
    }

    @Test
    void shouldReturnIndependentCopies() {
        registry.register(
                "monthly-report",
                createPrototype()
        );

        ReportTemplatePrototype first =
                registry.create(
                        "monthly-report"
                );

        ReportTemplatePrototype second =
                registry.create(
                        "monthly-report"
                );

        assertThat(first)
                .isNotSameAs(second);

        assertThat(first.getSections())
                .isNotSameAs(
                        second.getSections()
                );

        assertThat(first.getStyle())
                .isNotSameAs(
                        second.getStyle()
                );
    }

    @Test
    void shouldProtectStoredPrototypeFromCallerMutation() {
        ReportTemplatePrototype source =
                createPrototype();

        registry.register(
                "monthly-report",
                source
        );

        source.getSections()
                .get(0)
                .setTitle(
                        "Changed externally"
                );

        source.getParameters()
                .put(
                        "external",
                        "value"
                );

        ReportTemplatePrototype result =
                registry.create(
                        "monthly-report"
                );

        assertThat(
                result.getSections()
                        .get(0)
                        .getTitle()
        ).isEqualTo("Summary");

        assertThat(
                result.getParameters()
        ).doesNotContainKey(
                "external"
        );
    }

    @Test
    void shouldProtectStoredPrototypeFromCreatedCopyMutation() {
        registry.register(
                "monthly-report",
                createPrototype()
        );

        ReportTemplatePrototype first =
                registry.create(
                        "monthly-report"
                );

        first.getSections()
                .get(0)
                .setTitle("Modified");

        first.getParameters()
                .put("branchCode", "020");

        ReportTemplatePrototype second =
                registry.create(
                        "monthly-report"
                );

        assertThat(
                second.getSections()
                        .get(0)
                        .getTitle()
        ).isEqualTo("Summary");

        assertThat(
                second.getParameters()
        ).doesNotContainKey(
                "branchCode"
        );
    }

    @Test
    void shouldRejectDuplicateKey() {
        registry.register(
                "monthly-report",
                createPrototype()
        );

        assertThatThrownBy(
                () -> registry.register(
                        "monthly-report",
                        createPrototype()
                )
        )
                .isInstanceOf(
                        DuplicatePrototypeException.class
                )
                .hasMessageContaining(
                        "monthly-report"
                );
    }

    @Test
    void shouldRejectMissingPrototype() {
        assertThatThrownBy(
                () -> registry.create(
                        "missing-report"
                )
        )
                .isInstanceOf(
                        PrototypeNotFoundException.class
                )
                .hasMessageContaining(
                        "missing-report"
                );
    }

    @Test
    void shouldExposeUnmodifiableKeys() {
        registry.register(
                "monthly-report",
                createPrototype()
        );

        assertThatThrownBy(
                () -> registry
                        .registeredKeys()
                        .remove("monthly-report")
        )
                .isInstanceOf(
                        UnsupportedOperationException.class
                );
    }

    private static ReportTemplatePrototype
    createPrototype() {
        return new ReportTemplatePrototype(
                "monthly-report",
                "Monthly Report",
                "vi",
                List.of(
                        new ReportSection(
                                "summary",
                                "Summary",
                                true
                        )
                ),
                new LinkedHashMap<>(),
                new ReportStyle(
                        "Arial",
                        "#2563EB",
                        11
                )
        );
    }
}
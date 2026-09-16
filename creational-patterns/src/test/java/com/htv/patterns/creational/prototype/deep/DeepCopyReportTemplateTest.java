package com.htv.patterns.creational.prototype.deep;

import com.htv.patterns.creational.prototype.domain.ReportSection;
import com.htv.patterns.creational.prototype.domain.ReportStyle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeepCopyReportTemplateTest {

    @Test
    void shouldCreateIndependentObjectGraph() {
        DeepCopyReportTemplate original =
                createTemplate();

        DeepCopyReportTemplate copy =
                original.copy();

        assertThat(copy)
                .isNotSameAs(original);

        assertThat(copy.getSections())
                .isNotSameAs(
                        original.getSections()
                );

        assertThat(copy.getParameters())
                .isNotSameAs(
                        original.getParameters()
                );

        assertThat(copy.getStyle())
                .isNotSameAs(
                        original.getStyle()
                );

        assertThat(
                copy.getSections().get(0)
        ).isNotSameAs(
                original.getSections().get(0)
        );
    }

    @Test
    void shouldNotModifyOriginalWhenCopyChanges() {
        DeepCopyReportTemplate original =
                createTemplate();

        DeepCopyReportTemplate copy =
                original.copy();

        copy.getSections()
                .get(0)
                .setTitle(
                        "Modified Summary"
                );

        copy.getParameters()
                .put(
                        "branchCode",
                        "020"
                );

        copy.getStyle()
                .setPrimaryColor(
                        "#FF0000"
                );

        assertThat(
                original.getSections()
                        .get(0)
                        .getTitle()
        ).isEqualTo("Summary");

        assertThat(
                original.getParameters()
        ).doesNotContainKey(
                "branchCode"
        );

        assertThat(
                original.getStyle()
                        .getPrimaryColor()
        ).isEqualTo(
                "#2563EB"
        );
    }

    private static
    DeepCopyReportTemplate createTemplate() {
        List<ReportSection> sections =
                new ArrayList<>();

        sections.add(
                new ReportSection(
                        "summary",
                        "Summary",
                        true
                )
        );

        return new DeepCopyReportTemplate(
                "monthly-report",
                "Monthly Report",
                sections,
                new LinkedHashMap<>(),
                new ReportStyle(
                        "Arial",
                        "#2563EB",
                        11
                )
        );
    }
}
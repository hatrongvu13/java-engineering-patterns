package com.htv.patterns.creational.prototype.shallow;

import com.htv.patterns.creational.prototype.domain.ReportSection;
import com.htv.patterns.creational.prototype.domain.ReportStyle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ShallowCopyReportTemplateTest {

    @Test
    void shouldCreateDifferentOuterObject() {
        ShallowCopyReportTemplate original =
                createTemplate();

        ShallowCopyReportTemplate copy =
                original.copy();

        assertThat(copy)
                .isNotSameAs(original);

        assertThat(copy.getCode())
                .isEqualTo(original.getCode());

        assertThat(copy.getName())
                .isEqualTo(original.getName());
    }

    @Test
    void shouldShareNestedReferences() {
        ShallowCopyReportTemplate original =
                createTemplate();

        ShallowCopyReportTemplate copy =
                original.copy();

        assertThat(copy.getSections())
                .isSameAs(
                        original.getSections()
                );

        assertThat(copy.getParameters())
                .isSameAs(
                        original.getParameters()
                );

        assertThat(copy.getStyle())
                .isSameAs(
                        original.getStyle()
                );
    }

    @Test
    void shouldDemonstrateSharedMutationProblem() {
        ShallowCopyReportTemplate original =
                createTemplate();

        ShallowCopyReportTemplate copy =
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
        ).isEqualTo(
                "Modified Summary"
        );

        assertThat(
                original.getParameters()
        ).containsEntry(
                "branchCode",
                "020"
        );

        assertThat(
                original.getStyle()
                        .getPrimaryColor()
        ).isEqualTo(
                "#FF0000"
        );
    }

    private static
    ShallowCopyReportTemplate createTemplate() {
        List<ReportSection> sections =
                new ArrayList<>();

        sections.add(
                new ReportSection(
                        "summary",
                        "Summary",
                        true
                )
        );

        Map<String, String> parameters =
                new LinkedHashMap<>();

        return new ShallowCopyReportTemplate(
                "monthly-report",
                "Monthly Report",
                sections,
                parameters,
                new ReportStyle(
                        "Arial",
                        "#2563EB",
                        11
                )
        );
    }
}
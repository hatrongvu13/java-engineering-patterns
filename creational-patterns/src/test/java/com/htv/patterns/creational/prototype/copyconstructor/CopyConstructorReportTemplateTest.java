package com.htv.patterns.creational.prototype.copyconstructor;

import com.htv.patterns.creational.prototype.domain.ReportSection;
import com.htv.patterns.creational.prototype.domain.ReportStyle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CopyConstructorReportTemplateTest {

    @Test
    void shouldCreateDeepCopyThroughConstructor() {
        CopyConstructorReportTemplate original =
                createTemplate();

        CopyConstructorReportTemplate copy =
                new CopyConstructorReportTemplate(
                        original
                );

        assertThat(copy)
                .isNotSameAs(original);

        assertThat(copy.getSections())
                .isNotSameAs(
                        original.getSections()
                );

        assertThat(copy.getStyle())
                .isNotSameAs(
                        original.getStyle()
                );
    }

    @Test
    void shouldSupportCopyMethod() {
        CopyConstructorReportTemplate original =
                createTemplate();

        CopyConstructorReportTemplate copy =
                original.copy();

        assertThat(copy)
                .isNotSameAs(original);

        assertThat(copy.getCode())
                .isEqualTo(original.getCode());
    }

    @Test
    void shouldRejectNullCopySource() {
        assertThatThrownBy(
                () ->
                        new CopyConstructorReportTemplate(
                                null
                        )
        )
                .isInstanceOf(
                        NullPointerException.class
                )
                .hasMessage(
                        "source must not be null"
                );
    }

    @Test
    void shouldNotModifyOriginalThroughCopy() {
        CopyConstructorReportTemplate original =
                createTemplate();

        CopyConstructorReportTemplate copy =
                original.copy();

        copy.getSections()
                .get(0)
                .setEnabled(false);

        copy.getStyle()
                .setFontName("Roboto");

        assertThat(
                original.getSections()
                        .get(0)
                        .isEnabled()
        ).isTrue();

        assertThat(
                original.getStyle()
                        .getFontName()
        ).isEqualTo("Arial");
    }

    private static
    CopyConstructorReportTemplate createTemplate() {
        return new CopyConstructorReportTemplate(
                "monthly-report",
                "Monthly Report",
                new ArrayList<>(
                        List.of(
                                new ReportSection(
                                        "summary",
                                        "Summary",
                                        true
                                )
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
package com.htv.patterns.creational.prototype.shallow;

import com.htv.patterns.creational.prototype.domain.ReportSection;
import com.htv.patterns.creational.prototype.domain.ReportStyle;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ShallowCopyReportTemplate
        implements Cloneable {

    private String code;
    private String name;
    private List<ReportSection> sections;
    private Map<String, String> parameters;
    private ReportStyle style;

    public ShallowCopyReportTemplate(
            String code,
            String name,
            List<ReportSection> sections,
            Map<String, String> parameters,
            ReportStyle style
    ) {
        this.code = requireText(
                code,
                "code"
        );

        this.name = requireText(
                name,
                "name"
        );

        this.sections =
                Objects.requireNonNull(
                        sections,
                        "sections must not be null"
                );

        this.parameters =
                Objects.requireNonNull(
                        parameters,
                        "parameters must not be null"
                );

        this.style =
                Objects.requireNonNull(
                        style,
                        "style must not be null"
                );
    }

    public ShallowCopyReportTemplate copy() {
        try {
            return (
                    ShallowCopyReportTemplate
                    ) super.clone();
        } catch (
                CloneNotSupportedException exception
        ) {
            throw new AssertionError(
                    "Cloneable contract was violated",
                    exception
            );
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(
            String code
    ) {
        this.code = requireText(
                code,
                "code"
        );
    }

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = requireText(
                name,
                "name"
        );
    }

    public List<ReportSection> getSections() {
        return sections;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public ReportStyle getStyle() {
        return style;
    }

    private static String requireText(
            String value,
            String fieldName
    ) {
        Objects.requireNonNull(
                value,
                fieldName + " must not be null"
        );

        String normalized = value.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(
                    fieldName + " must not be blank"
            );
        }

        return normalized;
    }
}
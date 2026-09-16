package com.htv.patterns.creational.prototype.copyconstructor;

import com.htv.patterns.creational.prototype.domain.ReportSection;
import com.htv.patterns.creational.prototype.domain.ReportStyle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CopyConstructorReportTemplate {

    private String code;
    private String name;
    private final List<ReportSection> sections;
    private final Map<String, String> parameters;
    private ReportStyle style;

    public CopyConstructorReportTemplate(
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
                new ArrayList<>(
                        Objects.requireNonNull(
                                sections,
                                "sections must not be null"
                        )
                );

        this.parameters =
                new LinkedHashMap<>(
                        Objects.requireNonNull(
                                parameters,
                                "parameters must not be null"
                        )
                );

        this.style =
                Objects.requireNonNull(
                        style,
                        "style must not be null"
                );
    }

    public CopyConstructorReportTemplate(
            CopyConstructorReportTemplate source
    ) {
        Objects.requireNonNull(
                source,
                "source must not be null"
        );

        this.code = source.code;
        this.name = source.name;

        this.sections =
                new ArrayList<>();

        for (
                ReportSection section
                : source.sections
        ) {
            this.sections.add(
                    new ReportSection(section)
            );
        }

        this.parameters =
                new LinkedHashMap<>(
                        source.parameters
                );

        this.style =
                new ReportStyle(
                        source.style
                );
    }

    public CopyConstructorReportTemplate copy() {
        return new CopyConstructorReportTemplate(
                this
        );
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

    public void setStyle(
            ReportStyle style
    ) {
        this.style =
                Objects.requireNonNull(
                        style,
                        "style must not be null"
                );
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
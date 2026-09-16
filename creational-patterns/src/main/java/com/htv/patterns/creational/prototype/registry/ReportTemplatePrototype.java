package com.htv.patterns.creational.prototype.registry;

import com.htv.patterns.creational.prototype.domain.ReportSection;
import com.htv.patterns.creational.prototype.domain.ReportStyle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Deep-copy report template used by the Prototype Registry.
 */
public final class ReportTemplatePrototype
        implements Prototype<ReportTemplatePrototype> {

    private String code;
    private String name;
    private String language;

    private final List<ReportSection> sections;
    private final Map<String, String> parameters;

    private ReportStyle style;

    public ReportTemplatePrototype(
            String code,
            String name,
            String language,
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

        this.language = normalizeLanguage(
                language
        );

        this.sections =
                copySections(sections);

        this.parameters =
                new LinkedHashMap<>(
                        Objects.requireNonNull(
                                parameters,
                                "parameters must not be null"
                        )
                );

        this.style =
                new ReportStyle(
                        Objects.requireNonNull(
                                style,
                                "style must not be null"
                        )
                );
    }

    private ReportTemplatePrototype(
            ReportTemplatePrototype source
    ) {
        this(
                source.code,
                source.name,
                source.language,
                source.sections,
                source.parameters,
                source.style
        );
    }

    @Override
    public ReportTemplatePrototype copy() {
        return new ReportTemplatePrototype(
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(
            String language
    ) {
        this.language =
                normalizeLanguage(language);
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
                new ReportStyle(
                        Objects.requireNonNull(
                                style,
                                "style must not be null"
                        )
                );
    }

    private static List<ReportSection>
    copySections(
            List<ReportSection> sections
    ) {
        Objects.requireNonNull(
                sections,
                "sections must not be null"
        );

        List<ReportSection> copied =
                new ArrayList<>(
                        sections.size()
                );

        for (ReportSection section : sections) {
            copied.add(
                    new ReportSection(
                            Objects.requireNonNull(
                                    section,
                                    "sections must not contain null"
                            )
                    )
            );
        }

        return copied;
    }

    private static String normalizeLanguage(
            String value
    ) {
        return requireText(
                value,
                "language"
        ).toLowerCase();
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
package com.htv.patterns.creational.builder.validated;

import java.util.List;
import java.util.Objects;

public final class ReportValidationException
        extends RuntimeException {

    private final List<String> violations;

    public ReportValidationException(List<String> violations) {
        super(createMessage(violations));
        this.violations = List.copyOf(violations);
    }

    public List<String> violations() {
        return violations;
    }

    private static String createMessage(List<String> violations) {
        Objects.requireNonNull(violations, "violations must not be null");

        if (violations.isEmpty()) {
            throw new IllegalArgumentException("violations must not be empty");
        }

        return "Invalid report request: " + String.join("; ", violations);
    }
}
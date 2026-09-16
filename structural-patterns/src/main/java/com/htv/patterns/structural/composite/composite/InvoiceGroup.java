package com.htv.patterns.structural.composite.composite;

import com.htv.patterns.structural.composite.core.InvoiceComponent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Composite: a named group of {@link InvoiceComponent} children
 * (line items or nested groups). {@link #total()} and
 * {@link #lineCount()} fold recursively over the subtree.
 */
public final class InvoiceGroup implements InvoiceComponent {

    private final String label;
    private final List<InvoiceComponent> children =
            new ArrayList<>();

    public InvoiceGroup(
            String label
    ) {
        this.label = requireText(label);
    }

    public InvoiceGroup add(
            InvoiceComponent component
    ) {
        children.add(
                Objects.requireNonNull(
                        component,
                        "component must not be null"
                )
        );

        return this;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public BigDecimal total() {
        return children.stream()
                .map(InvoiceComponent::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public int lineCount() {
        return children.stream()
                .mapToInt(InvoiceComponent::lineCount)
                .sum();
    }

    public List<InvoiceComponent> children() {
        return List.copyOf(children);
    }

    private static String requireText(
            String value
    ) {
        Objects.requireNonNull(
                value,
                "label must not be null"
        );

        String normalized = value.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(
                    "label must not be blank"
            );
        }

        return normalized;
    }
}

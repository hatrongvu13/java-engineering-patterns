package com.htv.patterns.structural.composite.leaf;

import com.htv.patterns.structural.composite.core.InvoiceComponent;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Leaf: a single priced line on an invoice.
 */
public final class LineItem implements InvoiceComponent {

    private final String label;
    private final BigDecimal unitPrice;
    private final int quantity;

    public LineItem(
            String label,
            BigDecimal unitPrice,
            int quantity
    ) {
        this.label = requireText(label);

        this.unitPrice = Objects.requireNonNull(
                unitPrice,
                "unitPrice must not be null"
        );

        if (unitPrice.signum() < 0) {
            throw new IllegalArgumentException(
                    "unitPrice must not be negative"
            );
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "quantity must be positive"
            );
        }

        this.quantity = quantity;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public BigDecimal total() {
        return unitPrice.multiply(
                BigDecimal.valueOf(quantity)
        );
    }

    @Override
    public int lineCount() {
        return 1;
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

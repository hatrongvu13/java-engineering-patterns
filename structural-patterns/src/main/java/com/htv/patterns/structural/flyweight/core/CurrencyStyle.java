package com.htv.patterns.structural.flyweight.core;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Flyweight: holds the intrinsic, shareable formatting metadata
 * for one currency (symbol + fraction digits). Extrinsic state
 * (the amount) is passed in at format time, never stored.
 */
public final class CurrencyStyle {

    private final String currencyCode;
    private final String symbol;
    private final int fractionDigits;

    public CurrencyStyle(
            String currencyCode,
            String symbol,
            int fractionDigits
    ) {
        this.currencyCode = requireText(
                currencyCode,
                "currencyCode"
        );

        this.symbol = requireText(
                symbol,
                "symbol"
        );

        if (fractionDigits < 0) {
            throw new IllegalArgumentException(
                    "fractionDigits must not be negative"
            );
        }

        this.fractionDigits = fractionDigits;
    }

    public String currencyCode() {
        return currencyCode;
    }

    /**
     * Formats an extrinsic amount using this shared style.
     */
    public String format(
            BigDecimal amount
    ) {
        Objects.requireNonNull(
                amount,
                "amount must not be null"
        );

        return symbol + amount.setScale(
                fractionDigits,
                java.math.RoundingMode.HALF_UP
        ).toPlainString();
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

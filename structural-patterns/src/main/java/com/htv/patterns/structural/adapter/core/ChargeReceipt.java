package com.htv.patterns.structural.adapter.core;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Normalised charge reply produced by any adapter regardless of
 * the underlying provider's native response shape.
 */
public record ChargeReceipt(
        PaymentProvider provider,
        String providerTransactionId,
        String reference,
        BigDecimal amount,
        Currency currency
) {

    public ChargeReceipt {
        Objects.requireNonNull(
                provider,
                "provider must not be null"
        );

        providerTransactionId = requireText(
                providerTransactionId,
                "providerTransactionId"
        );

        reference = requireText(
                reference,
                "reference"
        );

        Objects.requireNonNull(
                amount,
                "amount must not be null"
        );

        Objects.requireNonNull(
                currency,
                "currency must not be null"
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

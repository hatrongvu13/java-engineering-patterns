package com.htv.patterns.structural.adapter.core;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Provider-neutral charge request. Value object validated in
 * its compact constructor.
 */
public record ChargeRequest(
        String reference,
        BigDecimal amount,
        Currency currency,
        String customerId
) {

    public ChargeRequest {
        reference = requireText(
                reference,
                "reference"
        );

        Objects.requireNonNull(
                amount,
                "amount must not be null"
        );

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "amount must be positive"
            );
        }

        Objects.requireNonNull(
                currency,
                "currency must not be null"
        );

        customerId = requireText(
                customerId,
                "customerId"
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

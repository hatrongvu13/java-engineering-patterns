package com.htv.patterns.structural.facade.core;

import com.htv.patterns.structural.adapter.core.PaymentProvider;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Simplified checkout request accepted by the {@code CheckoutFacade},
 * hiding the several subsystems the facade coordinates.
 */
public record CheckoutRequest(
        String orderReference,
        PaymentProvider provider,
        BigDecimal amount,
        String sourceCurrency,
        String targetCurrency,
        String customerId
) {

    public CheckoutRequest {
        orderReference = requireText(
                orderReference,
                "orderReference"
        );

        Objects.requireNonNull(
                provider,
                "provider must not be null"
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

        sourceCurrency = requireText(
                sourceCurrency,
                "sourceCurrency"
        );

        targetCurrency = requireText(
                targetCurrency,
                "targetCurrency"
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

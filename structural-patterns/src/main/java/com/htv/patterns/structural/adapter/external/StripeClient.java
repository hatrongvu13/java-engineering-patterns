package com.htv.patterns.structural.adapter.external;

/**
 * Simulated third-party "Stripe-style" SDK with a native API
 * that speaks in integer cents and its own intent object. This
 * is the adaptee the {@code StripeGatewayAdapter} wraps.
 */
public final class StripeClient {

    public StripeIntent createPaymentIntent(
            long amountInCents,
            String currencyCode,
            String customerRef
    ) {
        if (amountInCents <= 0) {
            throw new StripeException(
                    "amount_in_cents must be positive"
            );
        }

        return new StripeIntent(
                "pi_" + Long.toHexString(
                        System.nanoTime()
                ),
                amountInCents,
                currencyCode.toLowerCase(),
                customerRef
        );
    }

    public record StripeIntent(
            String id,
            long amountInCents,
            String currency,
            String customer
    ) {
    }

    public static final class StripeException
            extends RuntimeException {

        public StripeException(
                String message
        ) {
            super(message);
        }
    }
}

package com.htv.patterns.structural.adapter.external;

/**
 * Simulated third-party "PayPal-style" SDK whose native API
 * speaks in decimal-string amounts and returns its own
 * transaction object. Adaptee for {@code PaypalGatewayAdapter}.
 */
public final class PaypalClient {

    public PaypalTransaction executePayment(
            String decimalAmount,
            String currencyCode,
            String payerId
    ) {
        if (decimalAmount == null || decimalAmount.isBlank()) {
            throw new PaypalException(
                    "amount is required"
            );
        }

        return new PaypalTransaction(
                "PAY-" + Integer.toHexString(
                        System.identityHashCode(this)
                ) + "-" + Long.toHexString(
                        System.nanoTime()
                ),
                decimalAmount,
                currencyCode.toUpperCase(),
                payerId,
                "COMPLETED"
        );
    }

    public record PaypalTransaction(
            String transactionId,
            String amount,
            String currency,
            String payer,
            String state
    ) {
    }

    public static final class PaypalException
            extends RuntimeException {

        public PaypalException(
                String message
        ) {
            super(message);
        }
    }
}

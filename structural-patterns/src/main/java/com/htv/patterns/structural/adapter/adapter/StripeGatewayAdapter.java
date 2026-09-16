package com.htv.patterns.structural.adapter.adapter;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.structural.adapter.core.ChargeReceipt;
import com.htv.patterns.structural.adapter.core.ChargeRequest;
import com.htv.patterns.structural.adapter.core.PaymentGateway;
import com.htv.patterns.structural.adapter.core.PaymentProvider;
import com.htv.patterns.structural.adapter.external.StripeClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Object adapter: wraps {@link StripeClient} (which speaks in
 * integer cents) and exposes it through the unified
 * {@link PaymentGateway} port.
 */
public final class StripeGatewayAdapter
        implements PaymentGateway {

    private static final int CENTS_SCALE = 2;

    private final StripeClient client;

    public StripeGatewayAdapter(
            StripeClient client
    ) {
        this.client = Objects.requireNonNull(
                client,
                "client must not be null"
        );
    }

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.STRIPE;
    }

    @Override
    public Result<ChargeReceipt> charge(
            ChargeRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        return Result.of(() -> {
            long cents = request.amount()
                    .movePointRight(CENTS_SCALE)
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValueExact();

            StripeClient.StripeIntent intent =
                    client.createPaymentIntent(
                            cents,
                            request.currency().getCurrencyCode(),
                            request.customerId()
                    );

            BigDecimal normalisedAmount =
                    BigDecimal.valueOf(intent.amountInCents())
                            .movePointLeft(CENTS_SCALE);

            return new ChargeReceipt(
                    PaymentProvider.STRIPE,
                    intent.id(),
                    request.reference(),
                    normalisedAmount,
                    request.currency()
            );
        });
    }
}

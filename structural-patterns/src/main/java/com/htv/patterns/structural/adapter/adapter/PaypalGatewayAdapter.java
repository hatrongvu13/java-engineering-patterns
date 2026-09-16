package com.htv.patterns.structural.adapter.adapter;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.structural.adapter.core.ChargeReceipt;
import com.htv.patterns.structural.adapter.core.ChargeRequest;
import com.htv.patterns.structural.adapter.core.PaymentGateway;
import com.htv.patterns.structural.adapter.core.PaymentProvider;
import com.htv.patterns.structural.adapter.external.PaypalClient;

import java.util.Objects;

/**
 * Object adapter: wraps {@link PaypalClient} (which speaks in
 * decimal-string amounts) and exposes it through the unified
 * {@link PaymentGateway} port.
 */
public final class PaypalGatewayAdapter
        implements PaymentGateway {

    private final PaypalClient client;

    public PaypalGatewayAdapter(
            PaypalClient client
    ) {
        this.client = Objects.requireNonNull(
                client,
                "client must not be null"
        );
    }

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.PAYPAL;
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
            PaypalClient.PaypalTransaction transaction =
                    client.executePayment(
                            request.amount().toPlainString(),
                            request.currency().getCurrencyCode(),
                            request.customerId()
                    );

            return new ChargeReceipt(
                    PaymentProvider.PAYPAL,
                    transaction.transactionId(),
                    request.reference(),
                    request.amount(),
                    request.currency()
            );
        });
    }
}

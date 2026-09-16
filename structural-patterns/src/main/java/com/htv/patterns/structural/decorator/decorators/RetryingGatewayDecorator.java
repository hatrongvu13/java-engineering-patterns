package com.htv.patterns.structural.decorator.decorators;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.structural.adapter.core.ChargeReceipt;
import com.htv.patterns.structural.adapter.core.ChargeRequest;
import com.htv.patterns.structural.adapter.core.PaymentGateway;
import com.htv.patterns.structural.decorator.core.PaymentGatewayDecorator;

/**
 * Decorator that retries a failing charge up to {@code maxAttempts}
 * times before surfacing the last failure.
 */
public final class RetryingGatewayDecorator
        extends PaymentGatewayDecorator {

    private final int maxAttempts;

    public RetryingGatewayDecorator(
            PaymentGateway delegate,
            int maxAttempts
    ) {
        super(delegate);

        if (maxAttempts < 1) {
            throw new IllegalArgumentException(
                    "maxAttempts must be >= 1"
            );
        }

        this.maxAttempts = maxAttempts;
    }

    @Override
    public Result<ChargeReceipt> charge(
            ChargeRequest request
    ) {
        Result<ChargeReceipt> last =
                super.charge(request);

        for (int attempt = 2;
             last.isFailure() && attempt <= maxAttempts;
             attempt++) {
            last = super.charge(request);
        }

        return last;
    }
}

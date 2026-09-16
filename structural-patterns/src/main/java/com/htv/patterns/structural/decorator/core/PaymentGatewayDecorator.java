package com.htv.patterns.structural.decorator.core;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.structural.adapter.core.ChargeReceipt;
import com.htv.patterns.structural.adapter.core.ChargeRequest;
import com.htv.patterns.structural.adapter.core.PaymentGateway;
import com.htv.patterns.structural.adapter.core.PaymentProvider;

import java.util.Objects;

/**
 * Base decorator for {@link PaymentGateway}. Holds the wrapped
 * delegate and forwards by default; concrete decorators add one
 * cross-cutting concern each and call {@code super}/{@code delegate}.
 */
public abstract class PaymentGatewayDecorator
        implements PaymentGateway {

    protected final PaymentGateway delegate;

    protected PaymentGatewayDecorator(
            PaymentGateway delegate
    ) {
        this.delegate = Objects.requireNonNull(
                delegate,
                "delegate must not be null"
        );
    }

    @Override
    public PaymentProvider provider() {
        return delegate.provider();
    }

    @Override
    public Result<ChargeReceipt> charge(
            ChargeRequest request
    ) {
        return delegate.charge(request);
    }
}

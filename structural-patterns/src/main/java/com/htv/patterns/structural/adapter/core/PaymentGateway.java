package com.htv.patterns.structural.adapter.core;

import com.htv.patterns.core.result.Result;

/**
 * Unified payment port the application depends on. Concrete
 * adapters translate a {@link ChargeRequest} into a specific
 * provider SDK call and normalise the reply into a
 * {@link ChargeReceipt}.
 */
public interface PaymentGateway {

    PaymentProvider provider();

    Result<ChargeReceipt> charge(
            ChargeRequest request
    );
}

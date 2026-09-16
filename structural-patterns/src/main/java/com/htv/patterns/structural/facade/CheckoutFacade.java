package com.htv.patterns.structural.facade;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.structural.adapter.core.ChargeReceipt;
import com.htv.patterns.structural.adapter.core.ChargeRequest;
import com.htv.patterns.structural.adapter.core.PaymentGateway;
import com.htv.patterns.structural.adapter.core.PaymentProvider;
import com.htv.patterns.structural.facade.core.CheckoutRequest;
import com.htv.patterns.structural.proxy.core.ExchangeRateService;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.Objects;

/**
 * Facade over the payment subsystems: it converts the amount via
 * an {@link ExchangeRateService}, selects the right
 * {@link PaymentGateway} by provider, and issues the charge —
 * exposing a single {@link #checkout(CheckoutRequest)} call.
 */
public final class CheckoutFacade {

    private final ExchangeRateService exchangeRateService;
    private final Map<PaymentProvider, PaymentGateway> gateways;

    public CheckoutFacade(
            ExchangeRateService exchangeRateService,
            Map<PaymentProvider, PaymentGateway> gateways
    ) {
        this.exchangeRateService = Objects.requireNonNull(
                exchangeRateService,
                "exchangeRateService must not be null"
        );

        this.gateways = Map.copyOf(
                Objects.requireNonNull(
                        gateways,
                        "gateways must not be null"
                )
        );
    }

    public Result<ChargeReceipt> checkout(
            CheckoutRequest request
    ) {
        Objects.requireNonNull(
                request,
                "request must not be null"
        );

        return Result.of(() -> {
            PaymentGateway gateway =
                    gateways.get(request.provider());

            if (gateway == null) {
                throw new IllegalArgumentException(
                        "no gateway for provider "
                                + request.provider()
                );
            }

            BigDecimal rate =
                    request.sourceCurrency()
                            .equals(request.targetCurrency())
                            ? BigDecimal.ONE
                            : exchangeRateService.rate(
                                    request.sourceCurrency(),
                                    request.targetCurrency()
                            );

            BigDecimal converted =
                    request.amount().multiply(rate);

            ChargeRequest chargeRequest =
                    new ChargeRequest(
                            request.orderReference(),
                            converted,
                            Currency.getInstance(
                                    request.targetCurrency()
                            ),
                            request.customerId()
                    );

            return gateway.charge(chargeRequest)
                    .orElseGet(cause -> {
                        throw new IllegalStateException(
                                "charge failed: "
                                        + cause.getMessage(),
                                cause
                        );
                    });
        });
    }
}

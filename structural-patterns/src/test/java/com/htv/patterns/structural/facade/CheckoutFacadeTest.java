package com.htv.patterns.structural.facade;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.structural.adapter.adapter.StripeGatewayAdapter;
import com.htv.patterns.structural.adapter.core.ChargeReceipt;
import com.htv.patterns.structural.adapter.core.PaymentGateway;
import com.htv.patterns.structural.adapter.core.PaymentProvider;
import com.htv.patterns.structural.adapter.external.StripeClient;
import com.htv.patterns.structural.facade.core.CheckoutRequest;
import com.htv.patterns.structural.proxy.proxy.CachingExchangeRateProxy;
import com.htv.patterns.structural.proxy.real.RemoteExchangeRateService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CheckoutFacadeTest {

    private CheckoutFacade facade() {
        var rates =
                new CachingExchangeRateProxy(
                        new RemoteExchangeRateService(
                                Map.of(
                                        "USD->EUR",
                                        new BigDecimal("0.90")
                                )
                        )
                );

        Map<PaymentProvider, PaymentGateway> gateways =
                Map.of(
                        PaymentProvider.STRIPE,
                        new StripeGatewayAdapter(new StripeClient())
                );

        return new CheckoutFacade(rates, gateways);
    }

    @Test
    void shouldConvertCurrencyAndCharge() {
        Result<ChargeReceipt> result =
                facade().checkout(
                        new CheckoutRequest(
                                "order-9",
                                PaymentProvider.STRIPE,
                                new BigDecimal("100.00"),
                                "USD",
                                "EUR",
                                "cust-9"
                        )
                );

        assertThat(result.isSuccess()).isTrue();

        ChargeReceipt receipt = result.value();
        assertThat(receipt.amount())
                .isEqualByComparingTo("90.00");
        assertThat(receipt.currency().getCurrencyCode())
                .isEqualTo("EUR");
    }

    @Test
    void shouldFailForUnknownProvider() {
        Result<ChargeReceipt> result =
                facade().checkout(
                        new CheckoutRequest(
                                "order-10",
                                PaymentProvider.PAYPAL,
                                new BigDecimal("10.00"),
                                "USD",
                                "USD",
                                "cust-10"
                        )
                );

        assertThat(result.isFailure()).isTrue();
        assertThat(result.cause().getMessage())
                .contains("no gateway for provider");
    }
}

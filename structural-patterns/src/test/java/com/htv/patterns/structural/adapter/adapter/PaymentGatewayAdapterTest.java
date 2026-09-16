package com.htv.patterns.structural.adapter.adapter;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.structural.adapter.core.ChargeReceipt;
import com.htv.patterns.structural.adapter.core.ChargeRequest;
import com.htv.patterns.structural.adapter.core.PaymentProvider;
import com.htv.patterns.structural.adapter.external.PaypalClient;
import com.htv.patterns.structural.adapter.external.StripeClient;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentGatewayAdapterTest {

    private final Currency usd = Currency.getInstance("USD");

    private ChargeRequest request() {
        return new ChargeRequest(
                "order-1",
                new BigDecimal("19.99"),
                usd,
                "cust-1"
        );
    }

    @Test
    void stripeAdapterShouldTranslateAndNormalise() {
        var gateway =
                new StripeGatewayAdapter(new StripeClient());

        Result<ChargeReceipt> result =
                gateway.charge(request());

        assertThat(result.isSuccess()).isTrue();

        ChargeReceipt receipt = result.value();
        assertThat(receipt.provider())
                .isEqualTo(PaymentProvider.STRIPE);
        assertThat(receipt.providerTransactionId())
                .startsWith("pi_");
        assertThat(receipt.amount())
                .isEqualByComparingTo("19.99");
        assertThat(receipt.reference()).isEqualTo("order-1");
    }

    @Test
    void paypalAdapterShouldTranslateAndNormalise() {
        var gateway =
                new PaypalGatewayAdapter(new PaypalClient());

        Result<ChargeReceipt> result =
                gateway.charge(request());

        assertThat(result.isSuccess()).isTrue();

        ChargeReceipt receipt = result.value();
        assertThat(receipt.provider())
                .isEqualTo(PaymentProvider.PAYPAL);
        assertThat(receipt.providerTransactionId())
                .startsWith("PAY-");
        assertThat(receipt.amount())
                .isEqualByComparingTo("19.99");
    }

    @Test
    void adapterShouldReportProvider() {
        assertThat(
                new StripeGatewayAdapter(new StripeClient())
                        .provider()
        ).isEqualTo(PaymentProvider.STRIPE);

        assertThat(
                new PaypalGatewayAdapter(new PaypalClient())
                        .provider()
        ).isEqualTo(PaymentProvider.PAYPAL);
    }
}

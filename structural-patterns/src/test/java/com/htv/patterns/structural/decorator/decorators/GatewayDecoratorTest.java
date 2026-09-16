package com.htv.patterns.structural.decorator.decorators;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.structural.adapter.core.ChargeReceipt;
import com.htv.patterns.structural.adapter.core.ChargeRequest;
import com.htv.patterns.structural.adapter.core.PaymentGateway;
import com.htv.patterns.structural.adapter.core.PaymentProvider;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayDecoratorTest {

    private final Currency usd = Currency.getInstance("USD");

    private ChargeRequest request() {
        return new ChargeRequest(
                "ref-1",
                new BigDecimal("10.00"),
                usd,
                "cust-1"
        );
    }

    private ChargeReceipt receipt() {
        return new ChargeReceipt(
                PaymentProvider.STRIPE,
                "pi_1",
                "ref-1",
                new BigDecimal("10.00"),
                usd
        );
    }

    @Test
    void loggingDecoratorShouldRecordSuccessLine() {
        PaymentGateway gateway =
                new LoggingGatewayDecorator(
                        alwaysSucceeds()
                );

        Result<ChargeReceipt> result =
                gateway.charge(request());

        assertThat(result.isSuccess()).isTrue();
        assertThat(((LoggingGatewayDecorator) gateway).logLines())
                .containsExactly("OK STRIPE ref-1");
    }

    @Test
    void retryingDecoratorShouldSucceedAfterTransientFailures() {
        FlakyGateway flaky = new FlakyGateway(2);

        PaymentGateway gateway =
                new RetryingGatewayDecorator(flaky, 3);

        Result<ChargeReceipt> result =
                gateway.charge(request());

        assertThat(result.isSuccess()).isTrue();
        assertThat(flaky.calls()).isEqualTo(3);
    }

    @Test
    void decoratorsShouldStack() {
        FlakyGateway flaky = new FlakyGateway(1);

        LoggingGatewayDecorator gateway =
                new LoggingGatewayDecorator(
                        new RetryingGatewayDecorator(flaky, 2)
                );

        Result<ChargeReceipt> result =
                gateway.charge(request());

        assertThat(result.isSuccess()).isTrue();
        assertThat(gateway.logLines())
                .containsExactly("OK STRIPE ref-1");
    }

    private PaymentGateway alwaysSucceeds() {
        return new PaymentGateway() {
            @Override
            public PaymentProvider provider() {
                return PaymentProvider.STRIPE;
            }

            @Override
            public Result<ChargeReceipt> charge(
                    ChargeRequest request
            ) {
                return Result.success(receipt());
            }
        };
    }

    private final class FlakyGateway implements PaymentGateway {

        private final int failuresBeforeSuccess;
        private int calls;

        private FlakyGateway(
                int failuresBeforeSuccess
        ) {
            this.failuresBeforeSuccess = failuresBeforeSuccess;
        }

        int calls() {
            return calls;
        }

        @Override
        public PaymentProvider provider() {
            return PaymentProvider.STRIPE;
        }

        @Override
        public Result<ChargeReceipt> charge(
                ChargeRequest request
        ) {
            calls++;

            if (calls <= failuresBeforeSuccess) {
                return Result.failure("transient");
            }

            return Result.success(receipt());
        }
    }
}

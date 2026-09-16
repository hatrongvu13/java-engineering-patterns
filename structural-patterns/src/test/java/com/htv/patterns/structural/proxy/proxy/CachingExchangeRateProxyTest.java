package com.htv.patterns.structural.proxy.proxy;

import com.htv.patterns.structural.proxy.real.RemoteExchangeRateService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CachingExchangeRateProxyTest {

    private RemoteExchangeRateService realService() {
        return new RemoteExchangeRateService(
                Map.of(
                        "USD->EUR", new BigDecimal("0.92"),
                        "USD->GBP", new BigDecimal("0.79")
                )
        );
    }

    @Test
    void shouldServeRepeatLookupsFromCache() {
        RemoteExchangeRateService real = realService();
        CachingExchangeRateProxy proxy =
                new CachingExchangeRateProxy(real);

        assertThat(proxy.rate("USD", "EUR"))
                .isEqualByComparingTo("0.92");
        assertThat(proxy.rate("USD", "EUR"))
                .isEqualByComparingTo("0.92");
        assertThat(proxy.rate("USD", "EUR"))
                .isEqualByComparingTo("0.92");

        assertThat(real.lookupCount()).isEqualTo(1);
        assertThat(proxy.cachedPairs()).isEqualTo(1);
    }

    @Test
    void shouldCacheDistinctPairsSeparately() {
        RemoteExchangeRateService real = realService();
        CachingExchangeRateProxy proxy =
                new CachingExchangeRateProxy(real);

        proxy.rate("USD", "EUR");
        proxy.rate("USD", "GBP");

        assertThat(real.lookupCount()).isEqualTo(2);
        assertThat(proxy.cachedPairs()).isEqualTo(2);
    }

    @Test
    void shouldPropagateUnknownPairError() {
        CachingExchangeRateProxy proxy =
                new CachingExchangeRateProxy(realService());

        assertThatThrownBy(
                () -> proxy.rate("USD", "JPY")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("USD->JPY");
    }
}

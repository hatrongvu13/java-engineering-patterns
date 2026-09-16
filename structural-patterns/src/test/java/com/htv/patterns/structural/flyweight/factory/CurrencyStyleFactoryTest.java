package com.htv.patterns.structural.flyweight.factory;

import com.htv.patterns.structural.flyweight.core.CurrencyStyle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrencyStyleFactoryTest {

    @Test
    void shouldShareSameInstanceForSameCurrency() {
        CurrencyStyleFactory factory =
                new CurrencyStyleFactory();

        CurrencyStyle a = factory.styleFor("USD");
        CurrencyStyle b = factory.styleFor("usd");

        assertThat(a).isSameAs(b);
        assertThat(factory.pooledStyles()).isEqualTo(1);
    }

    @Test
    void shouldFormatExtrinsicAmount() {
        CurrencyStyleFactory factory =
                new CurrencyStyleFactory();

        assertThat(
                factory.styleFor("USD")
                        .format(new BigDecimal("9.5"))
        ).isEqualTo("$9.50");

        assertThat(
                factory.styleFor("JPY")
                        .format(new BigDecimal("1000"))
        ).isEqualTo("\u00a51000");
    }

    @Test
    void shouldRejectUnsupportedCurrency() {
        assertThatThrownBy(
                () -> new CurrencyStyleFactory().styleFor("XYZ")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unsupported currency XYZ");
    }
}

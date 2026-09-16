package com.htv.patterns.structural.proxy.real;

import com.htv.patterns.structural.proxy.core.ExchangeRateService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Real subject: simulates an expensive remote rate lookup and
 * counts how many times it was actually invoked, so a proxy's
 * caching can be verified.
 */
public final class RemoteExchangeRateService
        implements ExchangeRateService {

    private final Map<String, BigDecimal> table;
    private final AtomicInteger lookups = new AtomicInteger();

    public RemoteExchangeRateService(
            Map<String, BigDecimal> table
    ) {
        this.table = Map.copyOf(
                Objects.requireNonNull(
                        table,
                        "table must not be null"
                )
        );
    }

    @Override
    public BigDecimal rate(
            String from,
            String to
    ) {
        lookups.incrementAndGet();

        String key = from + "->" + to;
        BigDecimal rate = table.get(key);

        if (rate == null) {
            throw new IllegalArgumentException(
                    "no rate for " + key
            );
        }

        return rate;
    }

    public int lookupCount() {
        return lookups.get();
    }
}

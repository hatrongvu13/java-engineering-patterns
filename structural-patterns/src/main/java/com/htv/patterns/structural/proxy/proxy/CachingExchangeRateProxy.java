package com.htv.patterns.structural.proxy.proxy;

import com.htv.patterns.structural.proxy.core.ExchangeRateService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caching proxy: forwards the first lookup for a currency pair
 * to the wrapped {@link ExchangeRateService} and serves later
 * identical lookups from an in-memory cache.
 */
public final class CachingExchangeRateProxy
        implements ExchangeRateService {

    private final ExchangeRateService delegate;
    private final Map<String, BigDecimal> cache =
            new ConcurrentHashMap<>();

    public CachingExchangeRateProxy(
            ExchangeRateService delegate
    ) {
        this.delegate = Objects.requireNonNull(
                delegate,
                "delegate must not be null"
        );
    }

    @Override
    public BigDecimal rate(
            String from,
            String to
    ) {
        String key = from + "->" + to;

        return cache.computeIfAbsent(
                key,
                ignored -> delegate.rate(from, to)
        );
    }

    public int cachedPairs() {
        return cache.size();
    }
}

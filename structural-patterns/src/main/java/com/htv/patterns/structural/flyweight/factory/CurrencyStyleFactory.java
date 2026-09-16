package com.htv.patterns.structural.flyweight.factory;

import com.htv.patterns.structural.flyweight.core.CurrencyStyle;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Flyweight factory: returns a shared {@link CurrencyStyle} per
 * currency code, creating it once and reusing the same instance
 * for every later request.
 */
public final class CurrencyStyleFactory {

    private static final Map<String, StyleSpec> KNOWN = Map.of(
            "USD", new StyleSpec("$", 2),
            "EUR", new StyleSpec("\u20ac", 2),
            "JPY", new StyleSpec("\u00a5", 0),
            "GBP", new StyleSpec("\u00a3", 2)
    );

    private final Map<String, CurrencyStyle> pool =
            new ConcurrentHashMap<>();

    public CurrencyStyle styleFor(
            String currencyCode
    ) {
        Objects.requireNonNull(
                currencyCode,
                "currencyCode must not be null"
        );

        String code = currencyCode.trim().toUpperCase();

        return pool.computeIfAbsent(
                code,
                key -> {
                    StyleSpec spec = KNOWN.get(key);

                    if (spec == null) {
                        throw new IllegalArgumentException(
                                "unsupported currency " + key
                        );
                    }

                    return new CurrencyStyle(
                            key,
                            spec.symbol(),
                            spec.fractionDigits()
                    );
                }
        );
    }

    public int pooledStyles() {
        return pool.size();
    }

    private record StyleSpec(
            String symbol,
            int fractionDigits
    ) {
    }
}

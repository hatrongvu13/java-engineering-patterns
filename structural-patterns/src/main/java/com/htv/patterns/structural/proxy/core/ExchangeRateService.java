package com.htv.patterns.structural.proxy.core;

import java.math.BigDecimal;

/**
 * Subject: looks up the conversion rate between two currency
 * codes. The real implementation is assumed to be expensive
 * (remote call), so a proxy can add caching or access control.
 */
public interface ExchangeRateService {

    BigDecimal rate(
            String from,
            String to
    );
}

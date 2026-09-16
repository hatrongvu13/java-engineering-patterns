package com.htv.patterns.structural.composite.core;

import java.math.BigDecimal;

/**
 * Component of an invoice tree. Both a single {@code LineItem}
 * (leaf) and an {@code InvoiceGroup} (composite) implement this,
 * so a client can treat one item and a whole group uniformly.
 */
public interface InvoiceComponent {

    String label();

    BigDecimal total();

    int lineCount();
}

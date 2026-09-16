package com.htv.patterns.structural.composite.composite;

import com.htv.patterns.structural.composite.core.InvoiceComponent;
import com.htv.patterns.structural.composite.leaf.LineItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceCompositeTest {

    @Test
    void shouldSumNestedTreeRecursively() {
        InvoiceGroup hardware =
                new InvoiceGroup("hardware")
                        .add(new LineItem(
                                "keyboard",
                                new BigDecimal("50.00"),
                                2
                        ))
                        .add(new LineItem(
                                "mouse",
                                new BigDecimal("25.00"),
                                1
                        ));

        InvoiceGroup root =
                new InvoiceGroup("order")
                        .add(hardware)
                        .add(new LineItem(
                                "shipping",
                                new BigDecimal("10.00"),
                                1
                        ));

        assertThat(root.total())
                .isEqualByComparingTo("135.00");
        assertThat(root.lineCount()).isEqualTo(3);
    }

    @Test
    void shouldTreatLeafAndCompositeUniformly() {
        InvoiceComponent leaf =
                new LineItem(
                        "item",
                        new BigDecimal("5.00"),
                        3
                );

        InvoiceComponent group =
                new InvoiceGroup("g")
                        .add(new LineItem(
                                "item",
                                new BigDecimal("5.00"),
                                3
                        ));

        assertThat(leaf.total())
                .isEqualByComparingTo(group.total());
    }

    @Test
    void emptyGroupShouldTotalZero() {
        assertThat(new InvoiceGroup("empty").total())
                .isEqualByComparingTo("0");
    }
}

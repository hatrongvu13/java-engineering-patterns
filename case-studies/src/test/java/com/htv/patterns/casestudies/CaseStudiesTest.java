package com.htv.patterns.casestudies;

import com.htv.patterns.casestudies.orderfulfillment.*;
import com.htv.patterns.casestudies.loanapproval.*;
import com.htv.patterns.casestudies.eventprocessing.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class CaseStudiesTest {
    @Test
    void orderCompletes() {
        assertThat(new OrderFulfillmentCaseStudy().execute("1", true).status()).isEqualTo("COMPLETED");
    }

    @Test
    void orderCompensates() {
        assertThat(new OrderFulfillmentCaseStudy().execute("1", false).timeline()).endsWith("inventory-released");
    }

    @Test
    void riskyLoanRequiresReview() {
        assertThat(new LoanApprovalCaseStudy().execute("1", 600, 10).status()).isEqualTo("MANUAL_REVIEW");
    }

    @Test
    void duplicateEventIsIgnored() {
        var n = new AtomicInteger();
        var c = new EventProcessingCaseStudy();
        assertThat(c.consume("e1", "x", x -> n.incrementAndGet())).isTrue();
        assertThat(c.consume("e1", "x", x -> n.incrementAndGet())).isFalse();
        assertThat(n).hasValue(1);
    }
}

package com.htv.patterns.casestudies.orderfulfillment;

import com.htv.patterns.casestudies.shared.CaseStudyResult;

import java.util.*;

public final class OrderFulfillmentCaseStudy {
    public CaseStudyResult execute(String orderId, boolean paymentAvailable) {
        var log = new ArrayList<String>();
        log.add("command-received");
        log.add("inventory-reserved");
        if (!paymentAvailable) {
            log.add("payment-failed");
            log.add("inventory-released");
            return new CaseStudyResult(orderId, "COMPENSATED", log);
        }
        log.add("payment-captured");
        log.add("outbox-event-saved");
        log.add("shipment-created");
        return new CaseStudyResult(orderId, "COMPLETED", log);
    }
}

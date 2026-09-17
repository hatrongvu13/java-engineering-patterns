package com.htv.patterns.casestudies;

import com.htv.patterns.casestudies.orderfulfillment.OrderFulfillmentCaseStudy;
import com.htv.patterns.casestudies.loanapproval.LoanApprovalCaseStudy;

public final class CaseStudiesDemo {
    public static void main(String[] a) {
        System.out.println(new OrderFulfillmentCaseStudy().execute("ORD-001", true));
        System.out.println(new LoanApprovalCaseStudy().execute("LOAN-001", 720, 500_000_000L));
    }
}
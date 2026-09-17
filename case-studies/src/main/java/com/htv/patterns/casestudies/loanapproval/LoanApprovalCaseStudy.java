package com.htv.patterns.casestudies.loanapproval;

import com.htv.patterns.casestudies.shared.CaseStudyResult;

import java.util.*;

public final class LoanApprovalCaseStudy {
    public CaseStudyResult execute(String id, int score, long amount) {
        var log = new ArrayList<String>(List.of("application-validated", "credit-checked", "fraud-checked"));
        String status;
        if (score >= 700 && amount <= 1_000_000_000L) {
            status = "APPROVED";
            log.add("auto-approved");
        } else {
            status = "MANUAL_REVIEW";
            log.add("manual-review-requested");
        }
        return new CaseStudyResult(id, status, log);
    }
}

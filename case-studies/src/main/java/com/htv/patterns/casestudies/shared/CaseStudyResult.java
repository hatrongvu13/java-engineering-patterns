package com.htv.patterns.casestudies.shared;

import java.util.List;

public record CaseStudyResult(String id, String status, List<String> timeline) {
    public CaseStudyResult {
        timeline = List.copyOf(timeline);
    }
}
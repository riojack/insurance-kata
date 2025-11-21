package com.riojack.insurance.pojos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Policy(
        String policyId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal deductible,
        BigDecimal coverageLimit,
        List<String> coveredIncidents) {
}

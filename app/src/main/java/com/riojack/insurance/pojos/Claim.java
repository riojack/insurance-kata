package com.riojack.insurance.pojos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Claim(
        String policyId,
        String incidentType,
        LocalDateTime incidentDate,
        BigDecimal amountClaimed) {}

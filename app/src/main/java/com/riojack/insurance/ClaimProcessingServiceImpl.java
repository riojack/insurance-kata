package com.riojack.insurance;

import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Payout;
import com.riojack.insurance.pojos.Policy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ClaimProcessingServiceImpl implements ClaimProcessingService {
    private final List<Policy> policies;

    public ClaimProcessingServiceImpl(List<Policy> policies) {
        this.policies = policies;
    }

    @Override
    public Payout getClaimPayout(Claim claim) {
        Policy policy = getPolicy(claim);

        BigDecimal payoutAmount = calculatePayout(claim, policy);
        String reasonCode = getReasonCode(claim, policy, payoutAmount);
        boolean approved = "".equals(reasonCode);
        payoutAmount = approved ? payoutAmount : BigDecimal.ZERO;

        return new Payout(approved, payoutAmount, reasonCode);
    }

    private static String getReasonCode(Claim claim, Policy policy, BigDecimal payoutAmount) {
        String reason = "";

        if (isNonNegativeClaimAmount(claim) || isZeroPayAmount(payoutAmount)) {
            reason = "ZERO_PAYOUT";
        } else if (isOutsideCoveragePeriod(claim, policy)) {
            reason = "POLICY_INACTIVE";
        } else if (isNonCoveredIncident(claim, policy)) {
            reason = "NOT_COVERED";
        } else if (isPayoutOutsideOfCoverage(payoutAmount, policy)) {
            reason = "PAYOUT_EXCEEDS_COVERAGE";
        }

        return reason;
    }

    private static boolean isZeroPayAmount(BigDecimal payoutAmount) {
        return payoutAmount.compareTo(BigDecimal.ZERO) == 0;
    }

    private static boolean isNonCoveredIncident(Claim claim, Policy policy) {
        return !policy.coveredIncidents().contains(claim.incidentType());
    }

    private static boolean isOutsideCoveragePeriod(Claim claim, Policy policy) {
        LocalDateTime incDate = claim.incidentDate();
        return incDate.isBefore(policy.startDate()) || incDate.isAfter(policy.endDate());
    }

    private static boolean isNonNegativeClaimAmount(Claim claim) {
        return claim.amountClaimed().compareTo(BigDecimal.ZERO) < 0;
    }

    private static boolean isPayoutOutsideOfCoverage(BigDecimal payout, Policy policy) {
        return payout.compareTo(policy.coverageLimit()) > 0;
    }

    private Policy getPolicy(Claim claim) {
        return policies.stream()
                .filter(p -> p.policyId().equals(claim.policyId()))
                .findFirst()
                .get();
    }

    private static BigDecimal calculatePayout(Claim claim, Policy policy) {
        return claim.amountClaimed().subtract(policy.deductible());
    }
}

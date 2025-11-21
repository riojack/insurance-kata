package com.riojack.insurance;

import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Payout;
import com.riojack.insurance.pojos.Policy;
import java.math.BigDecimal;
import java.util.List;

public class ClaimProcessingServiceImpl implements ClaimProcessingService {
    private final List<Policy> policies;

    public ClaimProcessingServiceImpl(List<Policy> policies) {
        this.policies = policies;
    }

    @Override
    public Payout getClaimPayout(Claim claim) {
        if (isNonNegativeClaimAmount(claim)) {
            return new Payout(false, BigDecimal.ZERO, "ZERO_PAYOUT");
        }

        Policy policy = getPolicy(claim);
        boolean withinCoverageTimeframe = isWithinCoverageTimeframe(claim, policy);
        boolean withinCoverageAmount = isClaimWithinCoverage(claim, policy);

        if (withinCoverageTimeframe && withinCoverageAmount) {
            BigDecimal payoutAmount = calculatePayout(claim, policy);
            return new Payout(true, payoutAmount, "");
        } else if (!withinCoverageTimeframe) {
            return new Payout(false, BigDecimal.ZERO, "POLICY_INACTIVE");
        }

        return new Payout(false, BigDecimal.ZERO, "");
    }

    private static boolean isWithinCoverageTimeframe(Claim claim, Policy policy) {
        return claim.incidentDate().isAfter(policy.startDate())
                && claim.incidentDate().isBefore(policy.endDate());
    }

    private static boolean isNonNegativeClaimAmount(Claim claim) {
        return claim.amountClaimed().compareTo(BigDecimal.ZERO) < 0;
    }

    private static boolean isClaimWithinCoverage(Claim claim, Policy policy) {
        return claim.amountClaimed().compareTo(policy.coverageLimit()) < 0;
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

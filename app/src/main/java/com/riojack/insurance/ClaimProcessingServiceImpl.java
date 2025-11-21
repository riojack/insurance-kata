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
        Policy policy = getPolicy(claim);

        BigDecimal payoutAmount = calculatePayout(claim, policy);
        String reasonCode = ClaimValidator.getReasonCode(claim, policy, payoutAmount);
        boolean approved = "".equals(reasonCode);
        payoutAmount = approved ? payoutAmount : BigDecimal.ZERO;

        return new Payout(approved, payoutAmount, reasonCode);
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

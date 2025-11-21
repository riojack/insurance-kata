package com.riojack.insurance;

import com.riojack.insurance.exceptions.ClaimValidationException;
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
        if (claim.amountClaimed().compareTo(BigDecimal.ZERO) < 0) {
            throw new ClaimValidationException();
        }

        Policy policy = policies.stream().filter(p -> p.policyId().equals(claim.policyId())).findFirst().get();

        if (claim.amountClaimed().compareTo(policy.coverageLimit()) < 0) {
            return new Payout(true, claim.amountClaimed().add(BigDecimal.ZERO), "");
        }

        return new Payout(false, BigDecimal.ZERO, "");
    }
}

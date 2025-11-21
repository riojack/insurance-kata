package com.riojack.insurance;

import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Payout;
import com.riojack.insurance.pojos.Policy;
import java.math.BigDecimal;

public class ClaimProcessingServiceImpl implements ClaimProcessingService {
    private final PolicyRepository policyRepository;

    public ClaimProcessingServiceImpl(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public Payout getClaimPayout(Claim claim) {
        Policy policy = policyRepository.getPolicyById(claim.policyId());

        BigDecimal payoutAmount = calculatePayout(claim.amountClaimed(), policy);
        String reasonCode = ClaimValidator.getReasonCode(claim, policy, payoutAmount);
        boolean approved = "".equals(reasonCode);

        payoutAmount = approved ? payoutAmount : BigDecimal.ZERO;

        return new Payout(approved, payoutAmount, reasonCode);
    }

    private static BigDecimal calculatePayout(BigDecimal amountClaimed, Policy policy) {
        return amountClaimed.subtract(policy.deductible());
    }
}

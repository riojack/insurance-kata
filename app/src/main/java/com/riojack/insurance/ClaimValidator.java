package com.riojack.insurance;

import static com.riojack.insurance.constants.ReasonCodes.*;

import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Policy;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClaimValidator {
    private ClaimValidator() {}

    public static String getReasonCode(Claim claim, Policy policy, BigDecimal payoutAmount) {
        String reason = "";

        if (isNonNegativeClaimAmount(claim) || isZeroPayAmount(payoutAmount)) {
            reason = ZERO_PAYOUT;
        } else if (isOutsideCoveragePeriod(claim, policy)) {
            reason = POLICY_INACTIVE;
        } else if (isNonCoveredIncident(claim, policy)) {
            reason = NOT_COVERED;
        } else if (isPayoutOutsideOfCoverage(payoutAmount, policy)) {
            reason = PAYOUT_EXCEEDS_COVERAGE;
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
}

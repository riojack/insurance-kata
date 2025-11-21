package com.riojack.insurance;

import static com.riojack.insurance.TestData.POLICY_A;
import static com.riojack.insurance.TestData.POLICY_A_ID;
import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.*;

import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Payout;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClaimProcessingServiceImplTest {
    private ClaimProcessingService service;

    @BeforeEach
    public void setup() {
        service = new ClaimProcessingServiceImpl(of(POLICY_A));
    }

    @Test
    void whenAmountClaimedIsNegativeThenReturnZeroWithZeroPayoutReasonCode() {
        Claim claim =
                new Claim(POLICY_A_ID, "accident", LocalDateTime.now(), new BigDecimal("-0.01"));
        Payout payout = service.getClaimPayout(claim);
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("ZERO_PAYOUT", payout.reasonCode());
    }

    @Test
    void whenClaimAmountIsLessThanPolicyCoverageThenThePayoutIsMadeWithDeductibleSubtracted() {
        Claim claim =
                new Claim(POLICY_A_ID, "accident", LocalDateTime.now(), new BigDecimal("1000.00"));
        Payout payout = service.getClaimPayout(claim);

        BigDecimal expected = new BigDecimal("1000.00").subtract(POLICY_A.deductible());
        assertEquals(expected, payout.payout());
    }

    @Test
    void whenClaimAmountIsMoreThanPolicyCoverageThenPayoutIsNotMade() {
        Claim claim =
                new Claim(POLICY_A_ID, "accident", LocalDateTime.now(), new BigDecimal("6000.00"));
        Payout payout = service.getClaimPayout(claim);
        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
    }
}

package com.riojack.insurance;

import static com.riojack.insurance.TestData.CLAIM_A;
import static com.riojack.insurance.TestData.POLICY_A;
import static com.riojack.insurance.TestUtils.*;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Payout;
import java.math.BigDecimal;
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
        Claim claim = claimWithAmount(CLAIM_A, new BigDecimal("-0.01"));
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("ZERO_PAYOUT", payout.reasonCode());
    }

    @Test
    void whenAmountClaimedComesToZeroPayThenReturnZeroWithZeroPayoutReasonCode() {
        Claim claim = claimWithAmount(CLAIM_A, POLICY_A.deductible());
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("ZERO_PAYOUT", payout.reasonCode());
    }

    @Test
    void whenClaimAmountIsLessThanPolicyCoverageThenThePayoutIsMadeWithDeductibleSubtracted() {
        Claim claim = claimWithAmount(CLAIM_A, new BigDecimal("1000.00"));
        Payout payout = service.getClaimPayout(claim);

        BigDecimal expected = new BigDecimal("1000.00").subtract(POLICY_A.deductible());
        assertTrue(payout.approved());
        assertEquals(expected, payout.payout());
    }

    @Test
    void whenClaimAmountIsMoreThanPolicyCoverageThenPayoutIsNotMade() {
        Claim claim = claimWithAmount(CLAIM_A, new BigDecimal("6000.00"));
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
    }

    @Test
    void whenClaimIsBeforePolicyCoverageTimeframeThenPayoutNotGiven() {
        Claim claim = claimWithIncidentDate(CLAIM_A, POLICY_A.startDate().minusDays(2));
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("POLICY_INACTIVE", payout.reasonCode());
    }

    @Test
    void whenClaimIsAfterPolicyCoverageTimeframeThenPayoutNotGiven() {
        Claim claim = claimWithIncidentDate(CLAIM_A, POLICY_A.endDate().plusDays(2));
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("POLICY_INACTIVE", payout.reasonCode());
    }

    @Test
    void whenClaimIncidentTypeIsNotCoveredThenPayoutNotGiven() {
        Claim claim = claimWithIncidentType(CLAIM_A, "theft");
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("NOT_COVERED", payout.reasonCode());
    }
}

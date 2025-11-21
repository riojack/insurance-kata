package com.riojack.insurance;

import static com.riojack.insurance.TestData.*;
import static com.riojack.insurance.TestUtils.*;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Payout;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ClaimProcessingServiceImplTest {
    private ClaimProcessingService service;

    @BeforeEach
    public void setup() {
        service = new ClaimProcessingServiceImpl(of(POLICY_ALL_COVERAGE, POLICY_FIRE_COVERAGE));
    }

    @Test
    void whenAmountClaimedIsNegativeThenReturnZeroWithZeroPayoutReasonCode() {
        Claim claim = claimWithAmount(CLAIM, new BigDecimal("-0.01"));
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("ZERO_PAYOUT", payout.reasonCode());
    }

    @Test
    void whenAmountClaimedComesToZeroPayThenReturnZeroWithZeroPayoutReasonCode() {
        Claim claim = claimWithAmount(CLAIM, POLICY_ALL_COVERAGE.deductible());
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("ZERO_PAYOUT", payout.reasonCode());
    }

    @Test
    void whenClaimAmountIsLessThanPolicyCoverageThenThePayoutIsMadeWithDeductibleSubtracted() {
        Claim claim = claimWithAmount(CLAIM, new BigDecimal("1000.00"));
        Payout payout = service.getClaimPayout(claim);

        BigDecimal expected = new BigDecimal("1000.00").subtract(POLICY_ALL_COVERAGE.deductible());
        assertTrue(payout.approved());
        assertEquals(expected, payout.payout());
    }

    @Test
    void whenClaimAmountIsMoreThanPolicyCoverageThenPayoutIsNotMade() {
        Claim claim = claimWithAmount(CLAIM, new BigDecimal("6000.00"));
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
    }

    @Test
    void whenClaimIsBeforePolicyCoverageTimeframeThenPayoutNotGiven() {
        Claim claim = claimWithIncidentDate(CLAIM, POLICY_ALL_COVERAGE.startDate().minusDays(2));
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("POLICY_INACTIVE", payout.reasonCode());
    }

    @Test
    void whenClaimIsAfterPolicyCoverageTimeframeThenPayoutNotGiven() {
        Claim claim = claimWithIncidentDate(CLAIM, POLICY_ALL_COVERAGE.endDate().plusDays(2));
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("POLICY_INACTIVE", payout.reasonCode());
    }

    @ParameterizedTest
    @MethodSource("argsSubmittingClaimWithDifferentIncidentTypes")
    void whenSubmittingClaimWithDifferentIncidentTypes(
            String incidentType,
            boolean expectedApproved,
            BigDecimal expectedPayout,
            String expectedReasonCode) {
        Claim claim = claimWithIncidentType(CLAIM, incidentType);
        Payout payout = service.getClaimPayout(claim);

        assertEquals(expectedApproved, payout.approved());
        assertEquals(expectedPayout, payout.payout());
        assertEquals(expectedReasonCode, payout.reasonCode());
    }

    @Test
    void whenSubmittingClaimWithNoCoverageForIncidentThenNoPayoutGiven() {
        Claim claim = claimWithPolicyId(CLAIM, POLICY_FIRE_COVERAGE.policyId());
        claim = claimWithIncidentType(claim, "theft");
        Payout payout = service.getClaimPayout(claim);

        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
        assertEquals("NOT_COVERED", payout.reasonCode());
    }

    private static Stream<Arguments> argsSubmittingClaimWithDifferentIncidentTypes() {
        BigDecimal expectedPayout =
                CLAIM.amountClaimed().subtract(POLICY_ALL_COVERAGE.deductible());
        return Stream.of(
                Arguments.of("accident", true, expectedPayout, ""),
                Arguments.of("theft", true, expectedPayout, ""),
                Arguments.of("fire", true, expectedPayout, ""),
                Arguments.of("water damage", true, expectedPayout, ""),
                Arguments.of("tornado", false, BigDecimal.ZERO, "NOT_COVERED"));
    }
}

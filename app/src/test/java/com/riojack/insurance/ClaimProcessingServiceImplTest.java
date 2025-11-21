package com.riojack.insurance;

import static com.riojack.insurance.TestData.POLICY_A;
import static com.riojack.insurance.TestData.POLICY_A_ID;
import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.*;

import com.riojack.insurance.exceptions.ClaimValidationException;
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
    void whenAmountClaimedIsNegativeThenThrowException() {
        Claim claim =
                new Claim(POLICY_A_ID, "accident", LocalDateTime.now(), new BigDecimal("-0.01"));
        assertThrows(ClaimValidationException.class, () -> service.getClaimPayout(claim));
    }

    @Test
    void whenClaimAmountIsLessThanPolicyCoverageThenPayoutIsMade() {
        Claim claim =
                new Claim(POLICY_A_ID, "accident", LocalDateTime.now(), new BigDecimal("1000.00"));
        Payout payout = service.getClaimPayout(claim);
        assertEquals(new BigDecimal("1000.00"), payout.payout());
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

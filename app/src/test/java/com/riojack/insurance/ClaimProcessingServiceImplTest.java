package com.riojack.insurance;

import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.*;

import com.riojack.insurance.exceptions.ClaimValidationException;
import com.riojack.insurance.pojos.Claim;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.riojack.insurance.pojos.Payout;
import com.riojack.insurance.pojos.Policy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClaimProcessingServiceImplTest {
    public static final String POLICY_ID_1 = "POL001";
    private ClaimProcessingService service;

    @BeforeEach
    public void setup() {
        service = new ClaimProcessingServiceImpl(of(
                new Policy(
                        POLICY_ID_1,
                        LocalDateTime.of(2023, 1, 1, 0,0),
                        LocalDateTime.of(2024, 1, 1, 11,59),
                        new BigDecimal("500.00"),
                        new BigDecimal("5000.00"),
                        of("accident")
                )
        ));
    }

    @Test
    void whenAmountClaimedIsNegativeThenThrowException() {
        Claim claim = new Claim(POLICY_ID_1, "accident", LocalDateTime.now(), new BigDecimal("-0.01"));
        assertThrows(ClaimValidationException.class, () -> service.getClaimPayout(claim));
    }

    @Test
    void whenClaimAmountIsLessThanPolicyCoverageThenPayoutIsMade() {
        Claim claim = new Claim(POLICY_ID_1, "accident", LocalDateTime.now(), new BigDecimal("1000.00"));
        Payout payout = service.getClaimPayout(claim);
        assertEquals(new BigDecimal("1000.00"), payout.payout());
    }

    @Test
    void whenClaimAmountIsMoreThanPolicyCoverageThenPayoutIsNotMade() {
        Claim claim = new Claim(POLICY_ID_1, "accident", LocalDateTime.now(), new BigDecimal("6000.00"));
        Payout payout = service.getClaimPayout(claim);
        assertFalse(payout.approved());
        assertEquals(BigDecimal.ZERO, payout.payout());
    }
}

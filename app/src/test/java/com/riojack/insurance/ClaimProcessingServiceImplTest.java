package com.riojack.insurance;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.riojack.insurance.exceptions.ClaimValidationException;
import com.riojack.insurance.pojos.Claim;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClaimProcessingServiceImplTest {
    private ClaimProcessingService service;

    @BeforeEach
    public void setup() {
        service = new ClaimProcessingServiceImpl();
    }

    @Test
    void whenAmountClaimedIsNegativeThenThrowException() {
        Claim claim = new Claim("POL001", "accident", LocalDateTime.now(), new BigDecimal("-0.01"));
        assertThrows(ClaimValidationException.class, () -> service.getClaimPayout(claim));
    }
}

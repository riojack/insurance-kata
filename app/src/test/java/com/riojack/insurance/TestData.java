package com.riojack.insurance;

import static java.util.List.of;

import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Policy;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestData {
    private TestData() {}

    public static final String POLICY_A_ID = "POL001";
    public static final Policy POLICY_A =
            new Policy(
                    POLICY_A_ID,
                    LocalDateTime.of(2023, 1, 1, 0, 0),
                    LocalDateTime.of(2024, 1, 1, 11, 59),
                    new BigDecimal("500.00"),
                    new BigDecimal("5000.00"),
                    of("accident"));

    public static final Claim CLAIM_A =
            new Claim(
                    POLICY_A_ID,
                    "accident",
                    POLICY_A.startDate().plusDays(15),
                    new BigDecimal("1000.00"));
}

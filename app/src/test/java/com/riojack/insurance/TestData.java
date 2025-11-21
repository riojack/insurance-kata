package com.riojack.insurance;

import static java.util.List.of;

import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Policy;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestData {
    private TestData() {}

    public static final Policy POLICY_ALL_COVERAGE =
            new Policy(
                    "POL001",
                    LocalDateTime.of(2023, 1, 1, 0, 0),
                    LocalDateTime.of(2024, 1, 1, 11, 59),
                    new BigDecimal("500.00"),
                    new BigDecimal("5000.00"),
                    of("accident", "theft", "fire", "water damage"));

    public static final Policy POLICY_FIRE_COVERAGE =
            new Policy(
                    "POL002",
                    LocalDateTime.of(2023, 1, 1, 0, 0),
                    LocalDateTime.of(2024, 1, 1, 11, 59),
                    new BigDecimal("500.00"),
                    new BigDecimal("5000.00"),
                    of("fire"));

    public static final Claim CLAIM =
            new Claim(
                    "POL001",
                    "accident",
                    POLICY_ALL_COVERAGE.startDate().plusDays(15),
                    new BigDecimal("1000.00"));
}

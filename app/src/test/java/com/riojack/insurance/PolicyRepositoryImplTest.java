package com.riojack.insurance;

import static com.google.common.collect.ImmutableList.of;
import static com.riojack.insurance.TestData.POLICY_ALL_COVERAGE;
import static com.riojack.insurance.TestData.POLICY_FIRE_COVERAGE;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PolicyRepositoryImplTest {
    private PolicyRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new PolicyRepositoryImpl(of(POLICY_ALL_COVERAGE, POLICY_FIRE_COVERAGE));
    }

    @Test
    void whenPolicyNotFoundReturnsNull() {
        assertNull(repository.getPolicyById("DOES_NOT_EXIST"));
    }
}

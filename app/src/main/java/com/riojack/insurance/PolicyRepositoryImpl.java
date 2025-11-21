package com.riojack.insurance;

import com.riojack.insurance.pojos.Policy;
import java.util.List;

public class PolicyRepositoryImpl implements PolicyRepository {
    private final List<Policy> policies;

    public PolicyRepositoryImpl(List<Policy> policies) {
        this.policies = policies;
    }

    @Override
    public Policy getPolicyById(String policyId) {
        return policies.stream().filter(p -> p.policyId().equals(policyId)).findFirst().get();
    }
}

package com.riojack.insurance;

import com.riojack.insurance.pojos.Policy;

public interface PolicyRepository {
    Policy getPolicyById(String policyId);
}

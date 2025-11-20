package com.riojack.insurance;

import com.riojack.insurance.exceptions.ClaimValidationException;
import com.riojack.insurance.pojos.Claim;

public class ClaimProcessingServiceImpl implements ClaimProcessingService {
    @Override
    public void getClaimPayout(Claim claim) {
        throw new ClaimValidationException();
    }
}

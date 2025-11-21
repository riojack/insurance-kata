package com.riojack.insurance;

import com.riojack.insurance.exceptions.ClaimValidationException;
import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Payout;


public class ClaimProcessingServiceImpl implements ClaimProcessingService {
    @Override
    public Payout getClaimPayout(Claim claim) {
        throw new ClaimValidationException();
    }
}

package com.riojack.insurance;

import com.riojack.insurance.pojos.Claim;

public interface ClaimProcessingService {
    void getClaimPayout(Claim claim);
}

package com.riojack.insurance;

import com.riojack.insurance.pojos.Claim;
import com.riojack.insurance.pojos.Payout;

public interface ClaimProcessingService {
    Payout getClaimPayout(Claim claim);
}

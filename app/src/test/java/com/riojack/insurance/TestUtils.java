package com.riojack.insurance;

import com.riojack.insurance.pojos.Claim;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestUtils {
    private TestUtils() {}

    public static Claim claimWithAmount(Claim claim, BigDecimal amount) {
        return new Claim(claim.policyId(), claim.incidentType(), claim.incidentDate(), amount);
    }

    public static Claim claimWithIncidentDate(Claim claim, LocalDateTime incidentDate) {
        return new Claim(
                claim.policyId(), claim.incidentType(), incidentDate, claim.amountClaimed());
    }

    public static Claim claimWithIncidentType(Claim claim, String incidentType) {
        return new Claim(
                claim.policyId(), incidentType, claim.incidentDate(), claim.amountClaimed());
    }
}

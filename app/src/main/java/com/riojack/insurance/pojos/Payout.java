package com.riojack.insurance.pojos;

import java.math.BigDecimal;

public record Payout(boolean approved, BigDecimal payout, String reasonCode) {}

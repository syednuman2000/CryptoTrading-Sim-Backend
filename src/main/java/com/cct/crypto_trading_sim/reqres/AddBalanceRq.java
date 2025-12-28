package com.cct.crypto_trading_sim.reqres;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NonNull;

@Data
public class AddBalanceRq {
    @NonNull
    @Positive(message = "Amount to add must be greater that 0")
    private Double moneyToAdd;
}

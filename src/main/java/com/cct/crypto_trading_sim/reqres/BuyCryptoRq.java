package com.cct.crypto_trading_sim.reqres;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NonNull;

@Data
public class BuyCryptoRq {
    @NonNull
    private String cryptoId;
    @NonNull @Positive
    private Double amountInvested;
}

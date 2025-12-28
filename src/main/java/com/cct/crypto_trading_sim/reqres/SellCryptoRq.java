package com.cct.crypto_trading_sim.reqres;

import lombok.Data;
import lombok.NonNull;

@Data
public class SellCryptoRq {
    @NonNull
    private String cryptoId;
    @NonNull
    private Integer cryptoOrderId;
}

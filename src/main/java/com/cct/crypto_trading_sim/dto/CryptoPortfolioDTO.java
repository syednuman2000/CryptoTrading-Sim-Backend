package com.cct.crypto_trading_sim.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CryptoPortfolioDTO {
    private String cryptoId;
    private String cryptoName;
    private String cryptoSymbol;
    private Double cryptoBroughtMarketPrice;
    private Double cryptoBroughtQuantity;
    private Double amountInvested;
    private Double currentValue;
    private Double profitLoss;
    private String status;
    private Integer orderId;
    private LocalDateTime createdAt;
}

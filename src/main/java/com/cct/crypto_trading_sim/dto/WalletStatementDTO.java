package com.cct.crypto_trading_sim.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WalletStatementDTO {
    private String transactionType;
    private Double transactionAmount;
    private Double balanceAfterTransaction;
    private String fromAccount;
    private String toAccount;
    private LocalDateTime createdAt;
}

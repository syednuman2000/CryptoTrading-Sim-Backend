package com.cct.crypto_trading_sim.exception;

public class InsufficientFundsExeception extends RuntimeException {
    public InsufficientFundsExeception(String message) {
        super(message);
    }
}

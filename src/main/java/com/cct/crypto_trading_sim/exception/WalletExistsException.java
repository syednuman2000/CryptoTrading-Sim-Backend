package com.cct.crypto_trading_sim.exception;

public class WalletExistsException extends RuntimeException {
    public WalletExistsException(String message) {
        super(message);
    }
}

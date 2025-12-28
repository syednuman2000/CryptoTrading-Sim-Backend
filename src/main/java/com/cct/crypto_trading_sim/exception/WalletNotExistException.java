package com.cct.crypto_trading_sim.exception;

public class WalletNotExistException extends RuntimeException {
    public WalletNotExistException(String message) {
        super(message);
    }
}

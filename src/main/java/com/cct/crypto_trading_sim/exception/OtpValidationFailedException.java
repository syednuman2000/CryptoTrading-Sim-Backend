package com.cct.crypto_trading_sim.exception;

public class OtpValidationFailedException extends RuntimeException {
    public OtpValidationFailedException(String message) {
        super(message);
    }
}

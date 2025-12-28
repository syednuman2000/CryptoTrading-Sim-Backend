package com.cct.crypto_trading_sim.reqres;

import lombok.Data;

@Data
public class RegisterRq {
    private String username;
    private String password;
    private String fullName;
    private String phoneNo;
    private String email;
}

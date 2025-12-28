package com.cct.crypto_trading_sim.controller;

import com.cct.crypto_trading_sim.reqres.LoginRq;
import com.cct.crypto_trading_sim.reqres.RegisterRq;
import com.cct.crypto_trading_sim.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRq loginRq) {
        log.debug("entering LoginController#login username={}", loginRq.getUsername());
        ResponseEntity<?> response = loginService.login(loginRq.getUsername(), loginRq.getPassword());
        log.debug("exiting LoginController#login username={} status={}", loginRq.getUsername(), response != null ? response.getStatusCode() : "null");
        return response;
    }
}

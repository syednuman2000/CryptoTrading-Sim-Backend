package com.cct.crypto_trading_sim.controller;


import com.cct.crypto_trading_sim.reqres.RegisterRq;
import com.cct.crypto_trading_sim.reqres.CreateRs;
import com.cct.crypto_trading_sim.service.PreloginService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prelogin")
@RequiredArgsConstructor
public class PreloginController {

    private final PreloginService preloginService;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRq registerRq) {
        String username = (registerRq != null && registerRq.getUsername() != null) ? registerRq.getUsername() : "null";
        log.debug("entering PreloginController#register username={}", username);

        CreateRs createRs = preloginService.register(registerRq);
        log.debug("exiting PreloginController#register username={}", username);
        return ResponseEntity.ok().body(createRs);
    }
}

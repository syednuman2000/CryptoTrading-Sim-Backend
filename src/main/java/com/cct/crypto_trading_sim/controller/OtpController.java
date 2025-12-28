package com.cct.crypto_trading_sim.controller;

import com.cct.crypto_trading_sim.reqres.CreateRq;
import com.cct.crypto_trading_sim.reqres.CreateRs;
import com.cct.crypto_trading_sim.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    private static final Logger log = LoggerFactory.getLogger(OtpController.class);

    @GetMapping("/generateOtp")
    public ResponseEntity<?> generateOtp(@RequestBody CreateRq createRq) {
        log.debug("entering OtpController#generateOtp");
        CreateRs createRs = otpService.generateOtp(createRq);
        log.debug("exiting OtpController#generateOtp");
        return ResponseEntity.ok().body(createRs);
    }

    @PostMapping("/validateOtp")
    public ResponseEntity<?> validateOtp(@RequestBody CreateRq createRq) {
        log.debug("entering OtpController#validateOtp");
        CreateRs createRs = otpService.validateOtp(createRq);
        log.debug("exiting OtpController#validateOtp");
        return ResponseEntity.ok().body(createRs);
    }
}

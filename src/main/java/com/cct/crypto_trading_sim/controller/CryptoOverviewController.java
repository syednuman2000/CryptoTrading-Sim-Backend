package com.cct.crypto_trading_sim.controller;

import com.cct.crypto_trading_sim.reqres.OverviewRs;
import com.cct.crypto_trading_sim.service.CryptoOverviewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crypto-overview")
@RequiredArgsConstructor
public class CryptoOverviewController {

    private final CryptoOverviewService cryptoOverviewService;

    private static final Logger log = LoggerFactory.getLogger(CryptoOverviewController.class);

    @GetMapping("/prices")
    public ResponseEntity<?> getCryptoOverview() {
        log.debug("entering CryptoOverviewController#getCryptoOverview");
        OverviewRs overviewRs = cryptoOverviewService.getCryptoOverview();
        log.debug("exiting CryptoOverviewController#getCryptoOverview");
        return ResponseEntity.ok().body(overviewRs);
    }
}

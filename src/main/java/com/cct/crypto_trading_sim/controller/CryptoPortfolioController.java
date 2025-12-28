package com.cct.crypto_trading_sim.controller;

import com.cct.crypto_trading_sim.reqres.CryptoPortfolioRs;
import com.cct.crypto_trading_sim.service.CryptoPortfolioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crypto-portfolio")
@RequiredArgsConstructor
public class CryptoPortfolioController {

    private final CryptoPortfolioService cryptoPortfolioService;

    private static final Logger log = LoggerFactory.getLogger(CryptoPortfolioController.class);

    @GetMapping("/get-portfolio")
    public ResponseEntity<CryptoPortfolioRs> getCryptoPortfolio() {
        log.debug("entering CryptoPortfolioController#getCryptoPortfolio");
        CryptoPortfolioRs cryptoPortfolioRs = cryptoPortfolioService.getCryptoPortfolio();
        log.debug("exiting CryptoPortfolioController#getCryptoPortfolio");
        return ResponseEntity.ok().body(cryptoPortfolioRs);
    }
}

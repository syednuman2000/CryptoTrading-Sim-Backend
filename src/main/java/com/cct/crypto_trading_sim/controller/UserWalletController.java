package com.cct.crypto_trading_sim.controller;

import com.cct.crypto_trading_sim.reqres.AddBalanceRq;
import com.cct.crypto_trading_sim.reqres.BalanceRs;
import com.cct.crypto_trading_sim.reqres.CreateRs;
import com.cct.crypto_trading_sim.reqres.WalletStatementRs;
import com.cct.crypto_trading_sim.service.UserWalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class UserWalletController {
    private final UserWalletService userWalletService;

    private static final Logger log = LoggerFactory.getLogger(UserWalletController.class);

    @GetMapping("/create-wallet")
    public ResponseEntity<?> createWallet() {
        log.debug("entering WalletController#createWallet");
        CreateRs createRs = userWalletService.createWallet();
        log.debug("entering WalletController#createWallet");
        return ResponseEntity.ok().body(createRs);
    }

    @GetMapping("/get-balance")
    public ResponseEntity<?> getWalletBalance() {
        log.debug("entering WalletController#getWalletBalance");
        BalanceRs balanceRs = userWalletService.getWalletBalance();
        log.debug("exiting WalletController#getWalletBalance");
        return ResponseEntity.ok().body(balanceRs);
    }

    @GetMapping("/block-unblock-wallet")
    public ResponseEntity<?> blockUnblockWallet() {
        log.debug("entering WalletController#blockWallet");
        CreateRs createRs = userWalletService.blockUnblockWallet();
        log.debug("exiting WalletController#blockWallet");
        return ResponseEntity.ok().body(createRs);
    }

    @GetMapping("/get-wallet-statement")
    public ResponseEntity<?> getWalletStatement() {
        log.debug("entering WalletController#getWalletStatement");
        WalletStatementRs statement = userWalletService.getWalletStatement();
        log.debug("exiting WalletController#getWalletStatement");
        return ResponseEntity.ok().body(statement);
    }
}

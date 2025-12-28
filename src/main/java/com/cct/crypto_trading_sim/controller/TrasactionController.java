package com.cct.crypto_trading_sim.controller;

import com.cct.crypto_trading_sim.reqres.AddBalanceRq;
import com.cct.crypto_trading_sim.reqres.BalanceRs;
import com.cct.crypto_trading_sim.reqres.BuyCryptoRq;
import com.cct.crypto_trading_sim.reqres.SellCryptoRq;
import com.cct.crypto_trading_sim.service.TransactionService;
import com.cct.crypto_trading_sim.service.UserWalletService;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TrasactionController {
    private final TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(TrasactionController.class);

    @PostMapping("/add-money")
    public ResponseEntity<?> addMoneyToWallet(@RequestBody @Validated(Default.class) AddBalanceRq addBalanceRq){
        log.debug("entering WalletController#addMoneyToWallet");
        BalanceRs balanceRs = transactionService.addMoneyToWallet(addBalanceRq);
        log.debug("exiting WalletController#addMoneyToWallet");
        return ResponseEntity.ok().body(balanceRs);
    }

    @PostMapping("/buy-crypto")
    public ResponseEntity<?> buyCrypto(@RequestBody @Validated(Default.class) BuyCryptoRq buyCryptoRq) {
        log.debug("entering WalletController#buyCrypto");
        BalanceRs balanceRs = transactionService.buyCrypto(buyCryptoRq);
        log.debug("exiting WalletController#buyCrypto");
        return ResponseEntity.ok().body(balanceRs);
    }

    @PostMapping("/sell-crypto")
    public ResponseEntity<?> sellCrypto(@RequestBody @Validated(Default.class) SellCryptoRq sellCryptoRq) {
        log.debug("entering WalletController#sellCrypto");
        BalanceRs balanceRs = transactionService.sellCrypto(sellCryptoRq);
        log.debug("exiting WalletController#sellCrypto");
        return ResponseEntity.ok().body(balanceRs);
    }

}

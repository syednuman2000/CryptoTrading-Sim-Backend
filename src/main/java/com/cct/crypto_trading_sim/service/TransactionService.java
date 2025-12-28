package com.cct.crypto_trading_sim.service;

import com.cct.crypto_trading_sim.common.CommonHelperService;
import com.cct.crypto_trading_sim.dto.CryptoTableDTO;
import com.cct.crypto_trading_sim.entity.UserLogin;
import com.cct.crypto_trading_sim.entity.UserPortfolio;
import com.cct.crypto_trading_sim.entity.UserWallet;
import com.cct.crypto_trading_sim.entity.WalletStatemet;
import com.cct.crypto_trading_sim.enums.CryptoBuyStatus;
import com.cct.crypto_trading_sim.enums.WalletStatementEnum;
import com.cct.crypto_trading_sim.exception.WalletExistsException;
import com.cct.crypto_trading_sim.repository.UserPortfolioRepository;
import com.cct.crypto_trading_sim.repository.UserRepository;
import com.cct.crypto_trading_sim.repository.UserWalletRepository;
import com.cct.crypto_trading_sim.reqres.*;
import com.cct.crypto_trading_sim.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionService extends CommonHelperService {

    private final static String BANK_ACCOUNT = "BANK_ACCOUNT";

    private final CryptoOverviewService cryptoOverviewService;
    private final UserRepository userRepository;
    private final UserPortfolioRepository userPortfolioRepository;

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    public BalanceRs addMoneyToWallet(AddBalanceRq addBalanceRq) {
        log.debug("Entering getWalletBalance");
        Double balance = executeWalletTransaction(WalletStatementEnum.CREDIT, BANK_ACCOUNT, addBalanceRq.getMoneyToAdd());
        BalanceRs balanceRs = new BalanceRs();
        balanceRs.setBalance(balance);
        log.debug("New wallet balance after adding {} now is {}", addBalanceRq.getMoneyToAdd(), balanceRs.getBalance() );
        return balanceRs;
    }

    @Transactional
    public BalanceRs buyCrypto(BuyCryptoRq buyCryptoRq) {
        log.debug("Entering buyCrypto");

        OverviewRs cryptoOverview = cryptoOverviewService.getCryptoOverview();
        CryptoTableDTO cryptoTableDTO = cryptoOverview.getCryptoTableDTOS().stream()
                .filter(crypto -> crypto.getId().equalsIgnoreCase(buyCryptoRq.getCryptoId()))
                .findFirst().orElse(null);
        if(Objects.isNull(cryptoTableDTO)){
            throw new RuntimeException("Crypto with id " + buyCryptoRq.getCryptoId() + " not found");
        }
        executeBuyOrder(buyCryptoRq, cryptoTableDTO);
        Double balance = executeWalletTransaction(WalletStatementEnum.DEBIT, cryptoTableDTO.getName(), buyCryptoRq.getAmountInvested());

        BalanceRs balanceRs = new BalanceRs();
        balanceRs.setBalance(balance);
        log.debug("New wallet balance after buying crypto now is {}", balanceRs.getBalance() );
        return balanceRs;
    }

    @Transactional
    public BalanceRs sellCrypto(SellCryptoRq sellCryptoRq) {
        log.debug("Entering sellCrypto");
        OverviewRs cryptoOverview = cryptoOverviewService.getCryptoOverview();
        CryptoTableDTO cryptoTableDTO = cryptoOverview.getCryptoTableDTOS().stream()
                .filter(crypto -> crypto.getId().equalsIgnoreCase(sellCryptoRq.getCryptoId()))
                .findFirst().orElse(null);
        if(Objects.isNull(cryptoTableDTO)){
            throw new RuntimeException("Crypto with id " + sellCryptoRq.getCryptoId() + " not found");
        }

        Double sellingPrice = executeSellOrder(sellCryptoRq, cryptoTableDTO);
        Double balance = executeWalletTransaction(WalletStatementEnum.CREDIT, cryptoTableDTO.getName(), sellingPrice);

        BalanceRs balanceRs = new BalanceRs();
        balanceRs.setBalance(balance);
        log.debug("New wallet balance after buying crypto now is {}", balanceRs.getBalance() );
        return balanceRs;
    }

    private Double executeSellOrder(SellCryptoRq sellCryptoRq, CryptoTableDTO cryptoTableDTO){
        log.debug("Executing sell order for cryptoId={}", sellCryptoRq.getCryptoId());
        UserLogin userLogin = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElse(null);
        UserPortfolio userPortfolio = userPortfolioRepository.findTopByUserLoginIdAndCryptoIdAndOrderIdAndStatusOrderByCreatedAtDesc(
                userLogin.getId(), sellCryptoRq.getCryptoId(), sellCryptoRq.getCryptoOrderId(), CryptoBuyStatus.BROUGHT.name()).orElse(null);
        if(Objects.isNull(userPortfolio)){
            throw new RuntimeException("No bought crypto found to sell for cryptoId=" + sellCryptoRq.getCryptoId());
        }
        Double cryptoBroughtQuantity = userPortfolio.getCryptoBroughtQuantity();
        Double sellingPricePerUnit = cryptoTableDTO.getCurrentPrice().doubleValue();
        Double totalSellingAmount = sellingPricePerUnit * cryptoBroughtQuantity;

        userPortfolio.setStatus(CryptoBuyStatus.SOLD.name());
        userPortfolioRepository.save(userPortfolio);
        log.debug("Sell order executed for cryptoId={}. Total selling amount: {}", sellCryptoRq.getCryptoId(), totalSellingAmount);
        return totalSellingAmount;

    }

    private void executeBuyOrder(BuyCryptoRq buyCryptoRq, CryptoTableDTO cryptoTableDTO){
        log.debug("Adding entry in crypto portfolio for cryptoId={}", buyCryptoRq.getCryptoId());
        UserPortfolio userPortfolio = new UserPortfolio();
        UserLogin userLogin = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElse(null);
        Integer orderId = userPortfolioRepository.getCurrentMaxOrderIdForUser(userLogin.getId(), buyCryptoRq.getCryptoId());
        userPortfolio.setUserLogin(userLogin);
        BigDecimal cryptoPrice = cryptoTableDTO.getCurrentPrice();

        userPortfolio.setCryptoId(cryptoTableDTO.getId());
        userPortfolio.setCryptoName(cryptoTableDTO.getName());
        userPortfolio.setCryptoSymbol(cryptoTableDTO.getSymbol());
        userPortfolio.setCryptoBroughtMarketPrice(cryptoPrice.doubleValue());
        Double quantityBought = buyCryptoRq.getAmountInvested() / cryptoPrice.doubleValue();
        userPortfolio.setCryptoBroughtQuantity(quantityBought);
        userPortfolio.setAmountInvested(buyCryptoRq.getAmountInvested());
        userPortfolio.setStatus(CryptoBuyStatus.BROUGHT.name());
        userPortfolio.setOrderId(orderId+1);
        userPortfolio.setCreatedAt(java.time.LocalDateTime.now());
        userPortfolioRepository.save(userPortfolio);
        log.debug("Entry added in crypto portfolio for cryptoId={}", buyCryptoRq.getCryptoId());
    }
}

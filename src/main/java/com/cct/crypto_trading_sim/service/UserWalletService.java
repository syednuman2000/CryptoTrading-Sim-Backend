package com.cct.crypto_trading_sim.service;

import com.cct.crypto_trading_sim.common.CommonHelperService;
import com.cct.crypto_trading_sim.controller.LoginController;
import com.cct.crypto_trading_sim.custom.CustomUser;
import com.cct.crypto_trading_sim.dto.WalletStatementDTO;
import com.cct.crypto_trading_sim.entity.UserLogin;
import com.cct.crypto_trading_sim.entity.UserWallet;
import com.cct.crypto_trading_sim.entity.WalletStatemet;
import com.cct.crypto_trading_sim.enums.OtpServiceEnum;
import com.cct.crypto_trading_sim.enums.StatusEnum;
import com.cct.crypto_trading_sim.exception.SecurityBypassException;
import com.cct.crypto_trading_sim.exception.UserNotFoundException;
import com.cct.crypto_trading_sim.exception.WalletExistsException;
import com.cct.crypto_trading_sim.exception.WalletNotExistException;
import com.cct.crypto_trading_sim.repository.UserRepository;
import com.cct.crypto_trading_sim.repository.UserWalletRepository;
import com.cct.crypto_trading_sim.repository.WalletStatementRepository;
import com.cct.crypto_trading_sim.reqres.AddBalanceRq;
import com.cct.crypto_trading_sim.reqres.BalanceRs;
import com.cct.crypto_trading_sim.reqres.CreateRs;
import com.cct.crypto_trading_sim.reqres.WalletStatementRs;
import com.cct.crypto_trading_sim.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserWalletService extends CommonHelperService {
    private final UserWalletRepository userWalletRepository;
    private final UserRepository userRepository;
    private final WalletStatementRepository walletStatementRepository;

    private static final Logger log = LoggerFactory.getLogger(UserWalletService.class);

    public CreateRs createWallet() {
        log.debug("Entering createWallet");
        Long userId = SecurityUtil.getCurrentUserId();
        String username = SecurityUtil.getCurrentUsername();
        if(!checkForValidatedOtp(OtpServiceEnum.CREATE_WALLET.name())){
            throw new SecurityBypassException("Security bypass attempt detected for user="+ SecurityUtil.getCurrentUsername());
        }
        UserWallet wallet = userWalletRepository.findByUserLoginId(userId).orElse(null);
        if (wallet != null) {
            log.debug("Wallet already exists for userId={}", userId);
            throw new WalletExistsException("Wallet already exists for this user");
        }else{
            if(username==null){
                log.debug("Username is null for userId={}", userId);
                throw new UserNotFoundException("Username is null for userId="+ userId);
            }
            UserLogin userLogin = userRepository.findByUsername(username).orElse(null);
            UserWallet newWallet = new UserWallet();
            newWallet.setUserLogin(userLogin);
            newWallet.setBalance(0D);
            newWallet.setStatus(StatusEnum.ACTIVE.name());
            userWalletRepository.save(newWallet);
            log.debug("Wallet created for userId={}", userId);
        }
        log.debug("Exiting createWallet");
        return new CreateRs("Wallet created successfully");
    }

    public BalanceRs getWalletBalance() {
        log.debug("Entering getWalletBalance");
        Long userId = SecurityUtil.getCurrentUserId();
        UserWallet wallet = getWallet();
        BalanceRs balanceRs = new BalanceRs();
        balanceRs.setBalance(wallet.getBalance());
        return balanceRs;
    }

    public CreateRs blockUnblockWallet() {
        log.debug("Entering blockWallet");
        Long userId = SecurityUtil.getCurrentUserId();
        UserWallet wallet = getWallet();
        wallet.setStatus(wallet.getStatus().equalsIgnoreCase(StatusEnum.ACTIVE.name()) ? StatusEnum.INACTIVE.name() : StatusEnum.ACTIVE.name());
        userWalletRepository.save(wallet);
        log.debug("Wallet blocked for userId={}", userId);
        log.debug("Exiting blockWallet");
        return new CreateRs("Wallet blocked successfully");
    }

    public WalletStatementRs getWalletStatement() {
        log.debug("Entering getWalletStatement");
        Long userId = SecurityUtil.getCurrentUserId();
        List<WalletStatemet> walletStatements = walletStatementRepository.findAllByUserLoginIdOrderByCreatedAtDesc(userId);
        List<WalletStatementDTO> statements = mapWalletStatementsToDTOs(walletStatements);
        WalletStatementRs statementRs = new WalletStatementRs();
        statementRs.setStatements(statements);
        log.debug("Exiting getWalletStatement");
        return statementRs;
    }

    private List<WalletStatementDTO> mapWalletStatementsToDTOs(List<WalletStatemet> walletStatements){
        return walletStatements.stream().map(ws -> {
            WalletStatementDTO dto = new WalletStatementDTO();
            dto.setTransactionType(ws.getTransactionType());
            dto.setTransactionAmount(ws.getTransactionAmount());
            dto.setBalanceAfterTransaction(ws.getBalanceAfterTransaction());
            dto.setFromAccount(ws.getFromAccount());
            dto.setToAccount(ws.getToAccount());
            dto.setCreatedAt(ws.getCreatedAt());
            return dto;
        }).toList();
    }
}

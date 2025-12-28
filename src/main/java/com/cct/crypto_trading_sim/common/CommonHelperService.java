package com.cct.crypto_trading_sim.common;

import com.cct.crypto_trading_sim.entity.UserLogin;
import com.cct.crypto_trading_sim.entity.UserOtp;
import com.cct.crypto_trading_sim.entity.UserWallet;
import com.cct.crypto_trading_sim.entity.WalletStatemet;
import com.cct.crypto_trading_sim.enums.RecordStatusEnum;
import com.cct.crypto_trading_sim.enums.StatusEnum;
import com.cct.crypto_trading_sim.enums.WalletStatementEnum;
import com.cct.crypto_trading_sim.exception.InsufficientFundsExeception;
import com.cct.crypto_trading_sim.exception.WalletExistsException;
import com.cct.crypto_trading_sim.exception.WalletNotExistException;
import com.cct.crypto_trading_sim.repository.UserOtpRepository;
import com.cct.crypto_trading_sim.repository.UserRepository;
import com.cct.crypto_trading_sim.repository.UserWalletRepository;
import com.cct.crypto_trading_sim.repository.WalletStatementRepository;
import com.cct.crypto_trading_sim.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonHelperService {

    private static final String WALLET_ACCOUNT = "WALLET";

    private @Autowired UserWalletRepository userWalletRepository;
    private @Autowired UserRepository userRepository;
    private @Autowired WalletStatementRepository walletStatemetRepository;
    private @Autowired UserOtpRepository userOtpRepository;

    private static final Logger log = LoggerFactory.getLogger(CommonHelperService.class);

    public UserWallet getWallet(){
        Long userId = SecurityUtil.getCurrentUserId();
        UserWallet wallet = userWalletRepository.findByUserLoginId(userId).orElse(null);
        if (wallet != null) {
            log.debug("Exiting getWalletBalance with balance : {}", wallet.getBalance());
        }else{
            log.debug("No wallet found for userId={}", userId);
            throw new WalletNotExistException("No wallet found for userId = "+ userId);
        }
        return wallet;
    }

    public boolean isWalletBlocked(UserWallet wallet){
        return StatusEnum.INACTIVE.name().equalsIgnoreCase(wallet.getStatus());
    }

    public Double executeWalletTransaction(WalletStatementEnum walletStatementEnum, String accountUsed, Double transactionAmount){

        UserWallet wallet = getWallet();
        if(isWalletBlocked(wallet)){
            throw new WalletExistsException("Wallet is blocked for user = " + SecurityUtil.getCurrentUsername());
        }

        UserLogin userLogin = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElse(null);
        WalletStatemet walletStatemet = new WalletStatemet();
        walletStatemet.setUserLogin(userLogin);
        walletStatemet.setTransactionType(walletStatementEnum.name());
        walletStatemet.setTransactionAmount(transactionAmount);
        walletStatemet.setCreatedAt(java.time.LocalDateTime.now());

        if(walletStatementEnum == WalletStatementEnum.DEBIT){
            if(wallet.getBalance() < transactionAmount){
                log.debug("Insufficient funds in wallet:{} for this transaction:{}", wallet.getUserLogin().getUsername(), transactionAmount);
                throw new InsufficientFundsExeception("Insufficient funds in wallet for this transaction");
            }

            wallet.setBalance(wallet.getBalance() - transactionAmount);
            walletStatemet.setBalanceAfterTransaction(wallet.getBalance());
            walletStatemet.setFromAccount(WALLET_ACCOUNT);
            walletStatemet.setToAccount(accountUsed);
            log.debug("Debited amount {} from wallet. New balance is {}", transactionAmount, wallet.getBalance());
        }else if(walletStatementEnum == WalletStatementEnum.CREDIT){
            wallet.setBalance(wallet.getBalance() + transactionAmount);
            walletStatemet.setBalanceAfterTransaction(wallet.getBalance());
            walletStatemet.setFromAccount(accountUsed);
            walletStatemet.setToAccount(WALLET_ACCOUNT);
            log.debug("Credited amount {} to wallet. New balance is {}", transactionAmount, wallet.getBalance());
        }
        walletStatemetRepository.save(walletStatemet);
        userWalletRepository.save(wallet);
        return wallet.getBalance();
    }

    public boolean checkForValidatedOtp(String serviceCode){
        log.debug("Checking for validated OTP for serviceCode: {}", serviceCode);
        UserOtp userOtp = userOtpRepository.getLatestValidatedOtp(SecurityUtil.getCurrentUserId(), serviceCode);
        if(userOtp==null){
            return false;
        }
        userOtp.setStatus(RecordStatusEnum.COMPLETED.name());
        userOtpRepository.save(userOtp);
        log.debug("Exiting checkForValidated OTP for serviceCode: {}", serviceCode);
        return true;
    }
}

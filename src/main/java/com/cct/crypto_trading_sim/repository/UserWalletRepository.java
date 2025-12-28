package com.cct.crypto_trading_sim.repository;

import com.cct.crypto_trading_sim.entity.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {

    Optional<UserWallet> findByUserLoginId(Long userId);
}

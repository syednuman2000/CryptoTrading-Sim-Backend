package com.cct.crypto_trading_sim.repository;

import com.cct.crypto_trading_sim.entity.WalletStatemet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletStatementRepository extends JpaRepository<WalletStatemet, Long> {
    List<WalletStatemet> findAllByUserLoginIdOrderByCreatedAtDesc(Long userId);
}

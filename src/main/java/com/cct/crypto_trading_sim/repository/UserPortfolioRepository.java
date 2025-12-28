package com.cct.crypto_trading_sim.repository;

import com.cct.crypto_trading_sim.entity.UserPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserPortfolioRepository extends JpaRepository<UserPortfolio, Long> {
    @Query(value = "SELECT COALESCE(MAX(up.order_id), 0) FROM user_portfolio up  WHERE up.user_login = :userLoginId AND up.crypto_id = :cryptoId", nativeQuery = true)
    Integer getCurrentMaxOrderIdForUser(@Param("userLoginId") Long userLoginId, @Param("cryptoId") String cryptoId);

    Optional<UserPortfolio> findTopByUserLoginIdAndCryptoIdAndOrderIdAndStatusOrderByCreatedAtDesc(Long userLoginId, String cryptoId, Integer orderId, String status);

    List<UserPortfolio> findAllByUserLoginIdAndStatus(Long userLoginId, String status);
}

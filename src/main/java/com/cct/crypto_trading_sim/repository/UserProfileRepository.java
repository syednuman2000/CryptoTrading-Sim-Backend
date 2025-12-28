package com.cct.crypto_trading_sim.repository;

import com.cct.crypto_trading_sim.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserLoginId(Long id);
}

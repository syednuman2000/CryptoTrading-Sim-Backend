package com.cct.crypto_trading_sim.repository;

import com.cct.crypto_trading_sim.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {

    @Query(value = "SELECT * FROM user_otp uo where uo.user_login = :userLoginId AND uo.service_code = :serviceCode AND uo.status = 'PENDING' AND uo.expiration_time > CURRENT_TIMESTAMP ORDER BY uo.expiration_time DESC", nativeQuery = true)
    UserOtp getLatestPendingOtp(@Param("userLoginId") Long userLoginId, @Param("serviceCode") String serviceCode);

    @Query(value = "SELECT * FROM user_otp uo where uo.user_login = :userLoginId AND uo.service_code = :serviceCode AND uo.status = 'VALIDATED' AND uo.expiration_time > CURRENT_TIMESTAMP ORDER BY uo.expiration_time DESC", nativeQuery = true)
    UserOtp getLatestValidatedOtp(@Param("userLoginId") Long userLoginId, @Param("serviceCode") String serviceCode);

    @Query(value = "SELECT * FROM user_otp uo where uo.user_login = :userLoginId AND uo.service_code = :serviceCode AND (uo.status = 'PENDING' or uo.status = 'VALIDATED') AND uo.expiration_time > CURRENT_TIMESTAMP", nativeQuery = true)
    UserOtp getSameServiceCodeOtp(@Param("userLoginId") Long userLoginId, @Param("serviceCode") String serviceCode);

    @Query(value = "SELECT * FROM user_otp uo where uo.expiration_time <= CURRENT_TIMESTAMP AND (uo.status = 'PENDING' or uo.status = 'VALIDATED')", nativeQuery = true)
    List<UserOtp> getAllExpiredOtps();
}

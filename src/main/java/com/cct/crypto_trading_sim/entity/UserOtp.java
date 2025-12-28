package com.cct.crypto_trading_sim.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_otp")
public class UserOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_login", referencedColumnName = "id")
    private UserLogin userLogin;

    @Column(name = "service_code")
    private String serviceCode;

    @Column(name = "otp", nullable = false)
    private String otp;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    @Column(name = "status", nullable = false)
    private String status;
}

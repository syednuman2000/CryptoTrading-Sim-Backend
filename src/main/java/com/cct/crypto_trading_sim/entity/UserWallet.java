package com.cct.crypto_trading_sim.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_wallet")
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_login", referencedColumnName = "id")
    private UserLogin userLogin;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "status")
    private String status;
}

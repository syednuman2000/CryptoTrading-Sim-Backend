package com.cct.crypto_trading_sim.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "wallet_statement")
public class WalletStatemet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_login", referencedColumnName = "id")
    private UserLogin userLogin;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_amount")
    private Double transactionAmount;

    @Column(name = "balance_after_transaction")
    private Double balanceAfterTransaction;

    @Column(name = "from_account")
    private String fromAccount;

    @Column(name = "to_account")
    private String toAccount;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}

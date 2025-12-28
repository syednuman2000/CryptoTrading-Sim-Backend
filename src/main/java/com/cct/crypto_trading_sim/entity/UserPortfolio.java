package com.cct.crypto_trading_sim.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_portfolio")
public class UserPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_login", referencedColumnName = "id")
    private UserLogin userLogin;

    @Column(name = "crypto_id")
    private String cryptoId;

    @Column(name = "crypto_name")
    private String cryptoName;

    @Column(name = "crypto_symbol")
    private String cryptoSymbol;

    @Column(name = "crypto_brought_price")
    private Double cryptoBroughtMarketPrice;

    @Column(name = "crypto_brought_quantity")
    private Double cryptoBroughtQuantity;

    @Column(name = "amount_invested")
    private Double amountInvested;

    @Column(name = "status")
    private String status;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

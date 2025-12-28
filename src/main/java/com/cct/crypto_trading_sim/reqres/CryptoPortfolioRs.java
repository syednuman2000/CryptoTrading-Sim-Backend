package com.cct.crypto_trading_sim.reqres;

import com.cct.crypto_trading_sim.dto.CryptoPortfolioDTO;
import lombok.Data;

import java.util.List;

@Data
public class CryptoPortfolioRs {
    List<CryptoPortfolioDTO> portfolios;
}

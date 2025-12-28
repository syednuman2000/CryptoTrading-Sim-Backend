package com.cct.crypto_trading_sim.service;

import com.cct.crypto_trading_sim.controller.CryptoPortfolioController;
import com.cct.crypto_trading_sim.dto.CryptoPortfolioDTO;
import com.cct.crypto_trading_sim.entity.UserPortfolio;
import com.cct.crypto_trading_sim.enums.CryptoBuyStatus;
import com.cct.crypto_trading_sim.repository.UserPortfolioRepository;
import com.cct.crypto_trading_sim.repository.UserProfileRepository;
import com.cct.crypto_trading_sim.reqres.CryptoPortfolioRs;
import com.cct.crypto_trading_sim.reqres.OverviewRs;
import com.cct.crypto_trading_sim.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoPortfolioService {

    private final CryptoOverviewService cryptoOverviewService;
    private final UserPortfolioRepository userPortfolioRepository;

    private static final Logger log = LoggerFactory.getLogger(CryptoPortfolioService.class);

    public CryptoPortfolioRs getCryptoPortfolio() {
        log.debug("entering CryptoPortfolioService#getCryptoPortfolio");
        List<UserPortfolio> userPortfolios = userPortfolioRepository.findAllByUserLoginIdAndStatus(SecurityUtil.getCurrentUserId(), CryptoBuyStatus.BROUGHT.name());
        List<CryptoPortfolioDTO> cryptoPortfolioDTOS = mapUserPortfolioToCryptoPortfolioDTO(userPortfolios);
        CryptoPortfolioRs cryptoPortfolioRs = new CryptoPortfolioRs();
        cryptoPortfolioRs.setPortfolios(cryptoPortfolioDTOS);
        log.debug("exiting CryptoPortfolioService#getCryptoPortfolio");
        return cryptoPortfolioRs;
    }

    private List<CryptoPortfolioDTO> mapUserPortfolioToCryptoPortfolioDTO(List<UserPortfolio> userPortfolios){
        return userPortfolios.stream().map(userPortfolio -> {
            CryptoPortfolioDTO dto = new CryptoPortfolioDTO();
            dto.setCryptoId(userPortfolio.getCryptoId());
            dto.setCryptoName(userPortfolio.getCryptoName());
            dto.setCryptoSymbol(userPortfolio.getCryptoSymbol());
            dto.setCryptoBroughtMarketPrice(userPortfolio.getCryptoBroughtMarketPrice());
            dto.setCryptoBroughtQuantity(userPortfolio.getCryptoBroughtQuantity());
            dto.setAmountInvested(userPortfolio.getAmountInvested());
            dto.setStatus(userPortfolio.getStatus());
            dto.setOrderId(userPortfolio.getOrderId());
            dto.setCreatedAt(userPortfolio.getCreatedAt());
            return dto;
        }).toList();
    }
}

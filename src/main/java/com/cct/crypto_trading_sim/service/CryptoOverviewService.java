package com.cct.crypto_trading_sim.service;

import com.cct.crypto_trading_sim.common.CommonHelperService;
import com.cct.crypto_trading_sim.dto.CryptoTableDTO;
import com.cct.crypto_trading_sim.reqres.OverviewRs;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoOverviewService extends CommonHelperService {

    private static final String COINGECKO_URL = "https://api.coingecko.com/api/v3/coins/markets" +
            "?vs_currency=inr" +
            "&ids=bitcoin,ethereum,binancecoin,solana,ripple,cardano,dogecoin,tron,polkadot,polygon,litecoin,bitcoin-cash,chainlink,stellar,monero,cosmos,okb,internet-computer,uniswap,avalanche";
    private static final String CACHE_NAME = "mobileCache";
    private static final String CACHE_KEY = "'crypto_overview'";

    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(CryptoOverviewService.class);

    @Cacheable(value = CACHE_NAME, key = CACHE_KEY)
    public OverviewRs getCryptoOverview() {
        log.debug("Cache MISS – returning empty response");
        return null;
    }

    @CachePut(value = CACHE_NAME, key = CACHE_KEY)
    public OverviewRs getCryptoOverviewToCache() {
        CryptoTableDTO[] response = restTemplate.getForObject(COINGECKO_URL, CryptoTableDTO[].class);

        OverviewRs overviewRs = new OverviewRs();
        if (response != null) {
            overviewRs.setCryptoTableDTOS(List.of(response));
        }
        return overviewRs;
    }
}

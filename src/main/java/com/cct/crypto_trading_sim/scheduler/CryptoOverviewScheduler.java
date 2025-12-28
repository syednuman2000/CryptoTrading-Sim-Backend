package com.cct.crypto_trading_sim.scheduler;

import com.cct.crypto_trading_sim.dto.CryptoTableDTO;
import com.cct.crypto_trading_sim.reqres.OverviewRs;
import com.cct.crypto_trading_sim.service.CryptoOverviewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CryptoOverviewScheduler {

    private final CryptoOverviewService cryptoOverviewService;

    private static final Logger log = LoggerFactory.getLogger(CryptoOverviewScheduler.class);

    @Scheduled(fixedRate = 20000)
    public OverviewRs updateCryptoOverviewCache() {
        log.debug("Updating crypto overview cache");
        OverviewRs overviewRs = cryptoOverviewService.getCryptoOverviewToCache();
        log.debug("Crypto overview cache updated");
        return overviewRs;
    }
}

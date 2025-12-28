package com.cct.crypto_trading_sim.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class EhCacheConfig {
    @Bean
    public JCacheManagerCustomizer cacheCustomizer() {
        return cacheManager ->
            cacheManager.createCache(
                    "mobileCache",
                    Eh107Configuration.fromEhcacheCacheConfiguration(
                            CacheConfigurationBuilder
                                    .newCacheConfigurationBuilder(
                                            String.class,
                                            Object.class,
                                            ResourcePoolsBuilder
                                                    .newResourcePoolsBuilder()
                                                    .heap(10_000, EntryUnit.ENTRIES)
                                    )
                                    .withExpiry(
                                            ExpiryPolicyBuilder.timeToLiveExpiration(
                                                    Duration.ofSeconds(40)
                                            )
                                    )
                                    .build()
                    )
            );
    }
}

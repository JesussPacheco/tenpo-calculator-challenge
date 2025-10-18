package com.jesuspacheco.tenpo.infrastructure.adapter.out.cache;

import com.jesuspacheco.tenpo.domain.port.out.CacheProvider;
import com.jesuspacheco.tenpo.infrastructure.config.cache.CacheProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Adapter that implements CacheProvider port using Caffeine cache.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CaffeineCacheAdapter implements CacheProvider {

    private final CacheManager cacheManager;
    private final CacheProperties cacheProperties;

    @Override
    public Optional<BigDecimal> get(String key) {
        return Optional.ofNullable(getCache())
                .map(cache -> cache.get(key, BigDecimal.class))
                .map(value -> {
                    log.debug("Retrieved key={} value={}", key, value);
                    return value;
                });
    }

    @Override
    public void put(String key, BigDecimal value) {
        Optional.ofNullable(getCache())
                .ifPresent(cache -> {
                    cache.put(key, value);
                    log.debug("Stored key={} value={}", key, value);
                });
    }

    @Override
    public void evict(String key) {
        Optional.ofNullable(getCache())
                .ifPresent(cache -> {
                    cache.evict(key);
                    log.debug("Evicted key={}", key);
                });
    }

    private Cache getCache() {
        return cacheManager.getCache(cacheProperties.getCacheName());
    }
}
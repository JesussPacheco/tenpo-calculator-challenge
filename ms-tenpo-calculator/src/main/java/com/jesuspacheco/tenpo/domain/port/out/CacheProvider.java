package com.jesuspacheco.tenpo.domain.port.out;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Provider for caching percentage values.
 */
public interface CacheProvider {

    /**
     * Retrieves cached percentage value.
     *
     * @param key cache key
     * @return cached value if present
     */
    Optional<BigDecimal> get(String key);

    /**
     * Stores percentage value in cache.
     *
     * @param key   cache key
     * @param value percentage value to cache
     */
    void put(String key, BigDecimal value);

    /**
     * Removes value from cache.
     *
     * @param key cache key
     */
    void evict(String key);
}
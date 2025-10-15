package com.jesuspacheco.tenpo.application.percentage;

import com.jesuspacheco.tenpo.domain.constant.CacheKeys;
import com.jesuspacheco.tenpo.domain.exception.PercentageUnavailableException;
import com.jesuspacheco.tenpo.domain.port.out.CacheProvider;
import com.jesuspacheco.tenpo.domain.port.out.PercentageProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class PercentageService {
    private static final Predicate<BigDecimal> IS_VALID_PERCENTAGE =
            percentage -> percentage != null
                    && percentage.signum() >= 0
                    && percentage.compareTo(new BigDecimal("100")) <= 0;
    private final PercentageProvider percentageProvider;
    private final CacheProvider cacheProvider;

    public BigDecimal getPercentage() {
        return fetchFromProvider()
                .filter(IS_VALID_PERCENTAGE)
                .map(this::cacheAndReturn)
                .orElseGet(this::getCachedOrThrow);
    }

    private Optional<BigDecimal> fetchFromProvider() {
        try {
            return Optional.ofNullable(percentageProvider.getPercentage());
        } catch (Exception ex) {
            log.warn("External service unavailable: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private BigDecimal cacheAndReturn(BigDecimal percentage) {
        cacheProvider.put(CacheKeys.PERCENTAGE_LATEST, percentage);
        log.info("Percentage retrieved: {}", percentage);
        return percentage;
    }

    private BigDecimal getCachedOrThrow() {
        return cacheProvider.get(CacheKeys.PERCENTAGE_LATEST)
                .map(cached -> {
                    log.warn("Using cached percentage: {}", cached);
                    return cached;
                })
                .orElseThrow(() -> {
                    log.error("No cached percentage available");
                    return new PercentageUnavailableException("No cached value");
                });
    }
}

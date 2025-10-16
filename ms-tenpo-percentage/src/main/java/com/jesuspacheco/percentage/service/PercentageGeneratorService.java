package com.jesuspacheco.percentage.service;

import com.jesuspacheco.percentage.config.properties.PercentageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Service responsible for generating random percentage values.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PercentageGeneratorService {

    private final PercentageProperties properties;
    private final Random random = new Random();

    public BigDecimal generatePercentage() {
        BigDecimal range = properties.getMax().subtract(properties.getMin());
        BigDecimal randomValue = range.multiply(BigDecimal.valueOf(random.nextDouble()));
        BigDecimal percentage = properties.getMin().add(randomValue)
                .setScale(properties.getScale(), RoundingMode.HALF_UP);

        log.info("PERCENTAGE_GENERATOR: generated value={}", percentage);
        return percentage;
    }
}

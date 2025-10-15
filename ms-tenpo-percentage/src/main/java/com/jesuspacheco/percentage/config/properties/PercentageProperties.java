package com.jesuspacheco.percentage.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Configuration properties for percentage generation.
 */
@Component
@ConfigurationProperties(prefix = "app.percentage")
@Getter
@Setter
public class PercentageProperties {

    private BigDecimal min = new BigDecimal("5.00");
    private BigDecimal max = new BigDecimal("15.00");
    private Integer scale = 2;
}
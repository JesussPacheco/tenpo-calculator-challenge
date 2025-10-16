package com.jesuspacheco.tenpo.infrastructure.config.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Configuration properties for external percentage service.
 */
@Component
@ConfigurationProperties(prefix = "app.external.percentage")
@Getter
@Setter
public class PercentageClientProperties {

    private String url;
    private Duration timeout;
    private Integer maxRetries;
}
package com.jesuspacheco.tenpo.infrastructure.config.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Configuration properties for cache settings.
 */
@Component
@ConfigurationProperties(prefix = "app.cache")
@Getter
@Setter
public class CacheProperties {

    private Duration expiration;
    private Integer maximumSize;
    private String cacheName;
}
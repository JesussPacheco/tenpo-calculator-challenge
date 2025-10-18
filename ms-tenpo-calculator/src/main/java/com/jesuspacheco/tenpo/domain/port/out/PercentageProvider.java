package com.jesuspacheco.tenpo.domain.port.out;

import java.math.BigDecimal;

/**
 * Provider for obtaining percentage from external service.
 */
public interface PercentageProvider {

    /**
     * Fetches current percentage from external service.
     *
     * @return percentage value
     * @throws com.jesuspacheco.tenpo.domain.exception.ExternalServiceException if service fails
     */
    BigDecimal getPercentage();
}
package com.jesuspacheco.tenpo.infrastructure.adapter.out.client.dto;

import java.math.BigDecimal;

/**
 * Response DTO from external percentage service.
 */
public record PercentageResponse(
        BigDecimal percentage
) {
}
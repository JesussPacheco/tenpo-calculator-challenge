package com.jesuspacheco.percentage.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Response DTO containing generated percentage.
 */
@Schema(description = "Percentage response")
public record PercentageResponse(

        @Schema(description = "Generated percentage value", example = "10.50")
        BigDecimal percentage
) {}



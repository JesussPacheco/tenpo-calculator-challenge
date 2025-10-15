package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for calculation result.
 */
@Schema(description = "Calculation result with applied percentage")
public record CalculationResponse(

        @Schema(description = "First number", example = "100.50")
        BigDecimal num1,

        @Schema(description = "Second number", example = "200.75")
        BigDecimal num2,

        @Schema(description = "Applied percentage", example = "10.00")
        BigDecimal percentage,

        @Schema(description = "Final calculated result", example = "331.38")
        BigDecimal result,

        @Schema(description = "Calculation timestamp", example = "2025-10-12T18:30:00")
        LocalDateTime timestamp
) {
}
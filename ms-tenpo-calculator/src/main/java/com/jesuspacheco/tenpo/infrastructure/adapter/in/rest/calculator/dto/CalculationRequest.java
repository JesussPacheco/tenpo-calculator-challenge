package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request DTO for calculation endpoint.
 */
@Schema(description = "Request for sum calculation with percentage")
public record CalculationRequest(
        @NotNull(message = "num1 is required")
        @Digits(integer = 10, fraction = 2, message = "num1 must have max 10 integer digits and 2 decimal places")
        @DecimalMin(value = "0.00", message = "num1 must be positive")
        @DecimalMax(value = "9999999999.99", message = "num1 exceeds maximum allowed value")
        BigDecimal num1,

        @NotNull(message = "num2 is required")
        @Digits(integer = 10, fraction = 2, message = "num2 must have max 10 integer digits and 2 decimal places")
        @DecimalMin(value = "0.00", message = "num2 must be positive")
        @DecimalMax(value = "9999999999.99", message = "num2 exceeds maximum allowed value")
        BigDecimal num2
) {
}

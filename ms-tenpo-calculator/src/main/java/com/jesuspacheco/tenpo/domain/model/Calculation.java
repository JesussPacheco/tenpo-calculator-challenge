package com.jesuspacheco.tenpo.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model representing a calculation result.
 */
@Getter
@Builder
public class Calculation {
    private final BigDecimal num1;
    private final BigDecimal num2;
    private final BigDecimal percentage;
    private final BigDecimal result;
    private final LocalDateTime timestamp;
}
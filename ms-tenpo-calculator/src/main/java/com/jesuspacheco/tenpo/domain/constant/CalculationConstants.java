package com.jesuspacheco.tenpo.domain.constant;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Constants for calculation operations.
 */
public final class CalculationConstants {

    public static final BigDecimal PERCENTAGE_DIVISOR = new BigDecimal("100");
    public static final int RESULT_SCALE = 2;
    public static final int INTERMEDIATE_SCALE = 6;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private CalculationConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
}

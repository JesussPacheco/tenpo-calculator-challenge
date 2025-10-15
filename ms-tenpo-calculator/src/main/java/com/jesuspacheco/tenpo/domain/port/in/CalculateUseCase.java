package com.jesuspacheco.tenpo.domain.port.in;

import com.jesuspacheco.tenpo.domain.model.Calculation;

/**
 * Use case for calculating sum with dynamic percentage.
 */
public interface CalculateUseCase {

    /**
     * Executes calculation with percentage.
     * Input Calculation must contain num1 and num2.
     * Returns complete Calculation with percentage, result, and timestamp.
     */
    Calculation execute(Calculation input);
}
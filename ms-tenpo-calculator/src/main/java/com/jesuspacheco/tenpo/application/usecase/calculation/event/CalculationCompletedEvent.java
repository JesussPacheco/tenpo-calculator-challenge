package com.jesuspacheco.tenpo.application.usecase.calculation.event;

import com.jesuspacheco.tenpo.domain.model.Calculation;

/**
 * Event published when a calculation is successfully completed.
 */
public record CalculationCompletedEvent(
        Calculation calculation
) {
}

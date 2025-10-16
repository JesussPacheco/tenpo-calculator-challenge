package com.jesuspacheco.tenpo.application.usecase.calculation;

import com.jesuspacheco.tenpo.application.percentage.PercentageService;
import com.jesuspacheco.tenpo.application.usecase.calculation.event.CalculationCompletedEvent;
import com.jesuspacheco.tenpo.domain.constant.CalculationConstants;
import com.jesuspacheco.tenpo.domain.model.Calculation;
import com.jesuspacheco.tenpo.domain.port.in.CalculateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service responsible for calculation operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CalculatorService implements CalculateUseCase {

    private final PercentageService percentageService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Calculation execute(Calculation input) {

        log.debug("Calculating: {} + {}", input.getNum1(), input.getNum2());
        BigDecimal percentage = percentageService.getPercentage();
        BigDecimal result = calculateWithPercentage(input, percentage);

        Calculation calculation = createCalculation(input, percentage, result);
        eventPublisher.publishEvent(new CalculationCompletedEvent(calculation));

        log.info("Calculation completed: {} + {} with {}% = {}",
                input.getNum1(), input.getNum2(), percentage, result);

        return calculation;
    }

    private BigDecimal calculateWithPercentage(Calculation input, BigDecimal percentage) {
        BigDecimal sum = addNumbers(input.getNum1(), input.getNum2());
        BigDecimal multiplier = convertPercentageToMultiplier(percentage);
        return applyPercentage(sum, multiplier);
    }

    private BigDecimal convertPercentageToMultiplier(BigDecimal percentage) {
        BigDecimal percentageAsDecimal = percentage.divide(
                CalculationConstants.PERCENTAGE_DIVISOR,
                CalculationConstants.INTERMEDIATE_SCALE,
                CalculationConstants.ROUNDING_MODE
        );
        return BigDecimal.ONE.add(percentageAsDecimal);
    }

    private BigDecimal applyPercentage(BigDecimal amount, BigDecimal multiplier) {
        return amount.multiply(multiplier)
                .setScale(CalculationConstants.RESULT_SCALE,
                        CalculationConstants.ROUNDING_MODE);
    }

    private Calculation createCalculation(Calculation input,
                                          BigDecimal percentage,
                                          BigDecimal result) {
        return Calculation.builder()
                .num1(input.getNum1())
                .num2(input.getNum2())
                .percentage(percentage)
                .result(result)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private BigDecimal addNumbers(BigDecimal num1, BigDecimal num2) {
        return num1.add(num2);
    }
}

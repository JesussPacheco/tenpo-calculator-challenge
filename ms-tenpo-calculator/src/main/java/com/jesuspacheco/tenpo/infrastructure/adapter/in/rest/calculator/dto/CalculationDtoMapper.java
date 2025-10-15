package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.dto;

import com.jesuspacheco.tenpo.domain.model.Calculation;
import org.springframework.stereotype.Component;

/**
 * Mapper between Calculation domain objects and DTOs.
 */
@Component
public class CalculationDtoMapper {

    public Calculation toDomain(CalculationRequest request) {
        return Calculation.builder()
                .num1(request.num1())
                .num2(request.num2())
                .build();
    }

    public CalculationResponse toResponse(Calculation calculation) {
        return new CalculationResponse(
                calculation.getNum1(),
                calculation.getNum2(),
                calculation.getPercentage(),
                calculation.getResult(),
                calculation.getTimestamp()
        );
    }
}
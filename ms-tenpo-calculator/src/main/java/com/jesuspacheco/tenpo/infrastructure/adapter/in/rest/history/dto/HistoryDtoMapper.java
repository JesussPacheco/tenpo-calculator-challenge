package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.history.dto;

import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import org.springframework.stereotype.Component;

/**
 * Mapper between CalculationHistory domain objects and DTOs.
 */
@Component
public class HistoryDtoMapper {

    public HistoryResponse toResponse(CalculationHistory history) {
        return new HistoryResponse(
                history.getId(),
                history.getEndpoint(),
                history.getMethod(),
                history.getStatus(),
                history.getResponseType().getCode(),
                history.getRequestParams(),
                history.getOutcomeJson(),
                history.getExecutedAt()
        );
    }
}
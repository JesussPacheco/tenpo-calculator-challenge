package com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence.mapper;

import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import com.jesuspacheco.tenpo.domain.model.ResponseType;
import com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence.entity.HistoryEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between domain CalculationHistory and JPA HistoryEntity.
 */
@Component
public class HistoryEntityMapper {

    public HistoryEntity toEntity(CalculationHistory history) {
        return HistoryEntity.builder()
                .endpoint(history.getEndpoint())
                .method(history.getMethod())
                .status(history.getStatus())
                .responseTypeCode(history.getResponseType().getCode())
                .requestParams(history.getRequestParams())
                .outcomeJson(history.getOutcomeJson())
                .executedAt(history.getExecutedAt())
                .build();
    }

    public CalculationHistory toDomain(HistoryEntity entity) {
        return CalculationHistory.builder()
                .id(entity.getId())
                .endpoint(entity.getEndpoint())
                .method(entity.getMethod())
                .status(entity.getStatus())
                .responseType(ResponseType.fromCode(entity.getResponseTypeCode()))
                .requestParams(entity.getRequestParams())
                .outcomeJson(entity.getOutcomeJson())
                .executedAt(entity.getExecutedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
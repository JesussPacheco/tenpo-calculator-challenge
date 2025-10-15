package com.jesuspacheco.tenpo.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain model representing a history entry with full API call context.
 */
@Getter
@Builder
public class CalculationHistory {
    private final Long id;
    private final String endpoint;
    private final String method;
    private final Integer status;
    private final ResponseType responseType;
    private final Map<String, Object> requestParams;
    private final Map<String, Object> outcomeJson;
    private final LocalDateTime executedAt;
    private final LocalDateTime createdAt;
}
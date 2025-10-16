package com.jesuspacheco.tenpo.application.usecase.history.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import com.jesuspacheco.tenpo.domain.model.ResponseType;
import com.jesuspacheco.tenpo.infrastructure.filter.event.ApiErrorCalculationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApiErrorMapper {

    private final ObjectMapper objectMapper;

    public CalculationHistory toHistory(ApiErrorCalculationEvent event) {
        return CalculationHistory.builder()
                .endpoint(event.endpoint())
                .method(event.method())
                .status(event.statusCode())
                .responseType(ResponseType.ERROR)
                .requestParams(parseToMap(event.requestBody()))
                .outcomeJson(parseToMap(event.responseBody()))
                .executedAt(event.timestamp())
                .build();
    }

    private Map<String, Object> parseToMap(String json) {
        try {
            if (json == null || json.isEmpty()) {
                return Map.of();
            }
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            return Map.of("raw", json);
        }
    }
}

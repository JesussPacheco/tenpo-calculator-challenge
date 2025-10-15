package com.jesuspacheco.tenpo.infrastructure.filter.event;

import java.time.LocalDateTime;

public record ApiErrorCalculationEvent(
        String endpoint,
        String method,
        int statusCode,
        String requestBody,
        String responseBody,
        LocalDateTime timestamp
) {
}
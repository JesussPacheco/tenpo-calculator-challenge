package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.history.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response DTO for history entry.
 */
@Schema(description = "Historical calculation entry")
public record HistoryResponse(

        @Schema(description = "Entry ID", example = "1")
        Long id,

        @Schema(description = "API endpoint called", example = "/api/v1/calculator/sum")
        String endpoint,

        @Schema(description = "HTTP method", example = "POST")
        String method,

        @Schema(description = "HTTP status code", example = "200")
        Integer status,

        @Schema(description = "Response type", example = "SUCCESS")
        String responseType,

        @Schema(description = "Request parameters as JSON")
        Map<String, Object> requestParams,

        @Schema(description = "Outcome as JSON")
        Map<String, Object> outcomeJson,

        @Schema(description = "Execution timestamp", example = "2025-10-12T18:30:00")
        LocalDateTime executedAt
) {
}

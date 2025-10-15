package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Response DTO for error scenarios.
 */
@Schema(description = "Error response")
public record ErrorResponse(

        @Schema(description = "Error code", example = "CACHE_001")
        String code,

        @Schema(description = "Error message", example = "Percentage not found in cache")
        String message,

        @Schema(description = "Timestamp", example = "2025-10-12T18:30:00")
        LocalDateTime timestamp
) {
}

package com.jesuspacheco.tenpo.domain.model;

import lombok.Getter;

/**
 * Type of response from API operations.
 * Mirrors the response_type lookup table.
 */
@Getter
public enum ResponseType {
    SUCCESS("SUCCESS", "Successful operation"),
    ERROR("ERROR", "Operation failed");

    private final String code;
    private final String description;

    ResponseType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ResponseType fromCode(String code) {
        return switch (code) {
            case "SUCCESS" -> SUCCESS;
            case "ERROR" -> ERROR;
            default -> throw new IllegalStateException("Unknown response type code: " + code);
        };
    }
}
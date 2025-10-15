package com.jesuspacheco.tenpo.domain.exception;

import lombok.Getter;

/**
 * Enumeration of business error codes.
 * Each code represents a specific error scenario in the domain.
 */
@Getter
public enum ErrorCode {
    EXTERNAL_SERVICE_ERROR("EXT_001", "External percentage service returned error"),
    INVALID_CALCULATION_PARAMS("VAL_001", "Invalid calculation parameters provided"),
    PERCENTAGE_UNAVAILABLE("PCT_001", "Percentage value is unavailable"),
    HISTORY_UNAVAILABLE("HIS_001", "History service is unavailable");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}

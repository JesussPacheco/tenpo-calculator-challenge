package com.jesuspacheco.tenpo.domain.exception;

public class PercentageUnavailableException extends BusinessException {

    public PercentageUnavailableException() {
        super(ErrorCode.PERCENTAGE_UNAVAILABLE);
    }

    public PercentageUnavailableException(String message) {
        super(ErrorCode.PERCENTAGE_UNAVAILABLE, message);
    }
}
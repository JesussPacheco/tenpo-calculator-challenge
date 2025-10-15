package com.jesuspacheco.tenpo.domain.exception;

/**
 * Exception thrown when external percentage service fails.
 */
public class ExternalServiceException extends BusinessException {

    public ExternalServiceException(String message) {
        super(ErrorCode.EXTERNAL_SERVICE_ERROR, message);
    }

    public ExternalServiceException(Throwable cause) {
        super(ErrorCode.EXTERNAL_SERVICE_ERROR, cause);
    }
}
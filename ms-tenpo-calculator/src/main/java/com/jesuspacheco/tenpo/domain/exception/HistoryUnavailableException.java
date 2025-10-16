package com.jesuspacheco.tenpo.domain.exception;

public class HistoryUnavailableException extends BusinessException {

    public HistoryUnavailableException() {
        super(ErrorCode.HISTORY_UNAVAILABLE);
    }

    public HistoryUnavailableException(String message) {
        super(ErrorCode.HISTORY_UNAVAILABLE, message);
    }
}

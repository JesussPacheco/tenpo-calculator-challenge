package com.jesuspacheco.tenpo.infrastructure.exception;

import com.jesuspacheco.tenpo.domain.exception.ExternalServiceException;
import com.jesuspacheco.tenpo.domain.exception.HistoryUnavailableException;
import com.jesuspacheco.tenpo.domain.exception.PercentageUnavailableException;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.jesuspacheco.tenpo.infrastructure.exception.Errorcode.INVALID_JSON;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PercentageUnavailableException.class)
    public ResponseEntity<ErrorResponse> handlePercentageUnavailable(PercentageUnavailableException ex) {
        return buildErrorResponse(
                ex.getErrorCode().getCode(),
                "Percentage service is currently unavailable. Please try again later.",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @ExceptionHandler(HistoryUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleHistoryUnavailable(HistoryUnavailableException ex) {
        return buildErrorResponse(
                ex.getErrorCode().getCode(),
                "Calculation history is currently unavailable. Please try again later.",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceFailure(ExternalServiceException ex) {
        return buildErrorResponse(
                ex.getErrorCode().getCode(),
                ex.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArgument(IllegalArgumentException ex) {

        return buildErrorResponse(
                Errorcode.INVALID_ARGUMENTS,
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationFailure(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildErrorResponse(
                Errorcode.VALIDATION_ERROR,
                details,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Invalid JSON format: {}", ex.getMessage());
        return buildErrorResponse(
                INVALID_JSON,
                "Invalid JSON format in request body",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        String errorId = UUID.randomUUID().toString().substring(0, 8);
        log.error("Unexpected error [ID: {}]", errorId, ex);

        return buildErrorResponse(
                Errorcode.GENERIC_ERROR,
                "An unexpected error occurred. Reference: " + errorId,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String code,
            String message,
            HttpStatus status) {

        ErrorResponse error = new ErrorResponse(code, message, LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }
}
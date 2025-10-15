package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.history.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Get calculation history",
        description = "Retrieves paginated calculation history with all request/response details"
)
@ApiResponse(
        responseCode = "200",
        description = "History retrieved successfully"
)
public @interface HistoryApiDoc {
}
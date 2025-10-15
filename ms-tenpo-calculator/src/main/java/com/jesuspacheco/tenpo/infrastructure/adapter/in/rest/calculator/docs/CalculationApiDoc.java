package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Calculate sum with percentage",
        description = "Receives two numbers, sums them and applies a dynamic percentage from external service"
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Calculation successful"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid input parameters"
        ),
        @ApiResponse(
                responseCode = "503",
                description = "External service unavailable and no cache available"
        )
})
public @interface CalculationApiDoc {
}
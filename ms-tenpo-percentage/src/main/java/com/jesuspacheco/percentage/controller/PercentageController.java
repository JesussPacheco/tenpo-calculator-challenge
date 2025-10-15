package com.jesuspacheco.percentage.controller;

import com.jesuspacheco.percentage.dto.PercentageResponse;
import com.jesuspacheco.percentage.service.PercentageGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for percentage operations.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Percentage", description = "Percentage generation operations")
public class PercentageController {

    private final  PercentageGeneratorService percentageGeneratorService;

    @GetMapping("/percentage")
    @Operation(
            summary = "Get random percentage",
            description = "Returns a random percentage value between configured min and max"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Percentage generated successfully",
            content = @Content(schema = @Schema(implementation = PercentageResponse.class))
    )
    public ResponseEntity<PercentageResponse> getPercentage() {
        log.info("REST: received percentage request");

        PercentageResponse response = new PercentageResponse(
                percentageGeneratorService.generatePercentage()
        );

        log.info("REST: returning percentage={}", response.percentage());
        return ResponseEntity.ok(response);
    }
}

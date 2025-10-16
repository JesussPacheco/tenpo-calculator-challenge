package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator;

import com.jesuspacheco.tenpo.domain.model.Calculation;
import com.jesuspacheco.tenpo.domain.port.in.CalculateUseCase;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.docs.CalculationApiDoc;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.dto.CalculationDtoMapper;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.dto.CalculationRequest;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.dto.CalculationResponse;
import com.jesuspacheco.tenpo.infrastructure.constant.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for calculation operations.
 */
@RestController
@RequestMapping(ApiConstants.CALCULATOR_BASE_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Calculator", description = "Calculation operations with dynamic percentage")
public class CalculatorController {

    private final CalculateUseCase calculateUseCase;
    private final CalculationDtoMapper calculationDtoMapper;


    @PostMapping("/sum")
    @CalculationApiDoc
    public ResponseEntity<CalculationResponse> calculate(@Valid @RequestBody CalculationRequest request) {

        log.debug("Processing calculation request");

        Calculation input = calculationDtoMapper.toDomain(request);
        Calculation result = calculateUseCase.execute(input);
        CalculationResponse response = calculationDtoMapper.toResponse(result);
        return ResponseEntity.ok(response);
    }
}
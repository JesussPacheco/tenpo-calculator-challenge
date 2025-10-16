package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jesuspacheco.tenpo.domain.exception.PercentageUnavailableException;
import com.jesuspacheco.tenpo.domain.model.Calculation;
import com.jesuspacheco.tenpo.domain.port.in.CalculateUseCase;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.dto.CalculationDtoMapper;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.dto.CalculationRequest;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.calculator.dto.CalculationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorController.class)
@DisplayName("CalculatorController")
class CalculatorControllerTest {

    private static final String CALCULATOR_ENDPOINT = "/api/v1/calculator/sum";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CalculateUseCase calculateUseCase;
    @MockBean
    private CalculationDtoMapper calculationDtoMapper;

    @Test
    @DisplayName("should return 200 OK with valid calculation request")
    void shouldReturn200OkWithValidCalculationRequest() throws Exception {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("100.00"),
                new BigDecimal("200.00")
        );

        Calculation domainCalculation = Calculation.builder()
                .num1(request.num1())
                .num2(request.num2())
                .build();

        Calculation resultCalculation = Calculation.builder()
                .num1(request.num1())
                .num2(request.num2())
                .percentage(new BigDecimal("10.00"))
                .result(new BigDecimal("330.00"))
                .timestamp(LocalDateTime.now())
                .build();

        CalculationResponse response = new CalculationResponse(
                resultCalculation.getNum1(),
                resultCalculation.getNum2(),
                resultCalculation.getPercentage(),
                resultCalculation.getResult(),
                resultCalculation.getTimestamp()
        );

        when(calculationDtoMapper.toDomain(any(CalculationRequest.class)))
                .thenReturn(domainCalculation);
        when(calculateUseCase.execute(any(Calculation.class)))
                .thenReturn(resultCalculation);
        when(calculationDtoMapper.toResponse(any(Calculation.class)))
                .thenReturn(response);

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.num1").value(100.00))
                .andExpect(jsonPath("$.num2").value(200.00))
                .andExpect(jsonPath("$.percentage").value(10.00))
                .andExpect(jsonPath("$.result").value(330.00))
                .andExpect(jsonPath("$.timestamp").value(notNullValue()));
    }

    @Test
    @DisplayName("should return 400 Bad Request when num1 is null")
    void shouldReturn400BadRequestWhenNum1IsNull() throws Exception {
        String requestJson = "{\"num2\": 200.00}";

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("should return 400 Bad Request when num2 is null")
    void shouldReturn400BadRequestWhenNum2IsNull() throws Exception {
        String requestJson = "{\"num1\": 100.00}";

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("should return 400 Bad Request when num1 is negative")
    void shouldReturn400BadRequestWhenNum1IsNegative() throws Exception {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("-100.00"),
                new BigDecimal("200.00")
        );

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("should return 400 Bad Request when num2 is negative")
    void shouldReturn400BadRequestWhenNum2IsNegative() throws Exception {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("100.00"),
                new BigDecimal("-200.00")
        );

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("should return 400 Bad Request when num1 exceeds maximum value")
    void shouldReturn400BadRequestWhenNum1ExceedsMaximumValue() throws Exception {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("99999999999.99"),
                new BigDecimal("200.00")
        );

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("should return 400 Bad Request when num1 has more than 2 decimal places")
    void shouldReturn400BadRequestWhenNum1HasMoreThan2DecimalPlaces() throws Exception {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("100.999"),
                new BigDecimal("200.00")
        );

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("should return 400 Bad Request when num2 has more than 2 decimal places")
    void shouldReturn400BadRequestWhenNum2HasMoreThan2DecimalPlaces() throws Exception {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("100.00"),
                new BigDecimal("200.123")
        );

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("should return 503 Service Unavailable when percentage service fails")
    void shouldReturn503ServiceUnavailableWhenPercentageServiceFails() throws Exception {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("100.00"),
                new BigDecimal("200.00")
        );

        Calculation domainCalculation = Calculation.builder()
                .num1(request.num1())
                .num2(request.num2())
                .build();

        when(calculationDtoMapper.toDomain(any(CalculationRequest.class)))
                .thenReturn(domainCalculation);
        when(calculateUseCase.execute(any(Calculation.class)))
                .thenThrow(new PercentageUnavailableException("No cached value"));

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("PCT_001"));
    }

    @Test
    @DisplayName("should return 400 Bad Request with empty request body")
    void shouldReturn400BadRequestWithEmptyRequestBody() throws Exception {
        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 Bad Request with invalid JSON")
    void shouldReturn400BadRequestWithInvalidJson() throws Exception {
        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should accept boundary values correctly")
    void shouldAcceptBoundaryValuesCorrectly() throws Exception {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("0.00"),
                new BigDecimal("0.00")
        );

        Calculation domainCalculation = Calculation.builder()
                .num1(request.num1())
                .num2(request.num2())
                .build();

        Calculation resultCalculation = Calculation.builder()
                .num1(request.num1())
                .num2(request.num2())
                .percentage(new BigDecimal("10.00"))
                .result(new BigDecimal("0.00"))
                .timestamp(LocalDateTime.now())
                .build();

        CalculationResponse response = new CalculationResponse(
                resultCalculation.getNum1(),
                resultCalculation.getNum2(),
                resultCalculation.getPercentage(),
                resultCalculation.getResult(),
                resultCalculation.getTimestamp()
        );

        when(calculationDtoMapper.toDomain(any(CalculationRequest.class)))
                .thenReturn(domainCalculation);
        when(calculateUseCase.execute(any(Calculation.class)))
                .thenReturn(resultCalculation);
        when(calculationDtoMapper.toResponse(any(Calculation.class)))
                .thenReturn(response);

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(0.00));
    }

    @Test
    @DisplayName("should handle decimal numbers correctly")
    void shouldHandleDecimalNumbersCorrectly() throws Exception {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("50.50"),
                new BigDecimal("25.25")
        );

        Calculation domainCalculation = Calculation.builder()
                .num1(request.num1())
                .num2(request.num2())
                .build();

        Calculation resultCalculation = Calculation.builder()
                .num1(request.num1())
                .num2(request.num2())
                .percentage(new BigDecimal("5.00"))
                .result(new BigDecimal("79.54"))
                .timestamp(LocalDateTime.now())
                .build();

        CalculationResponse response = new CalculationResponse(
                resultCalculation.getNum1(),
                resultCalculation.getNum2(),
                resultCalculation.getPercentage(),
                resultCalculation.getResult(),
                resultCalculation.getTimestamp()
        );

        when(calculationDtoMapper.toDomain(any(CalculationRequest.class)))
                .thenReturn(domainCalculation);
        when(calculateUseCase.execute(any(Calculation.class)))
                .thenReturn(resultCalculation);
        when(calculationDtoMapper.toResponse(any(Calculation.class)))
                .thenReturn(response);

        mockMvc.perform(post(CALCULATOR_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.num1").value(50.50))
                .andExpect(jsonPath("$.num2").value(25.25))
                .andExpect(jsonPath("$.result").value(79.54));
    }
}

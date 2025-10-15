package com.jesuspacheco.tenpo.application.usecase.calculation;

import com.jesuspacheco.tenpo.application.percentage.PercentageService;
import com.jesuspacheco.tenpo.application.usecase.calculation.event.CalculationCompletedEvent;
import com.jesuspacheco.tenpo.domain.model.Calculation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CalculatorService")
class CalculatorServiceTest {

    @Mock
    private PercentageService percentageService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CalculatorService calculatorService;

    @Captor
    private ArgumentCaptor<CalculationCompletedEvent> eventCaptor;

    private Calculation inputCalculation;

    @BeforeEach
    void setUp() {
        inputCalculation = Calculation.builder()
                .num1(new BigDecimal("100.00"))
                .num2(new BigDecimal("200.00"))
                .build();
    }

    @Test
    @DisplayName("should calculate sum with percentage correctly")
    void shouldCalculateSumWithPercentageCorrectly() {
        BigDecimal percentage = new BigDecimal("10.00");
        when(percentageService.getPercentage()).thenReturn(percentage);

        Calculation result = calculatorService.execute(inputCalculation);

        assertThat(result.getResult()).isEqualByComparingTo("330.00");
        assertThat(result.getNum1()).isEqualByComparingTo("100.00");
        assertThat(result.getNum2()).isEqualByComparingTo("200.00");
        assertThat(result.getPercentage()).isEqualByComparingTo("10.00");
    }

    @Test
    @DisplayName("should calculate with decimal numbers correctly")
    void shouldCalculateWithDecimalNumbersCorrectly() {
        Calculation decimalInput = Calculation.builder()
                .num1(new BigDecimal("50.50"))
                .num2(new BigDecimal("25.25"))
                .build();

        BigDecimal percentage = new BigDecimal("5.00");
        when(percentageService.getPercentage()).thenReturn(percentage);

        Calculation result = calculatorService.execute(decimalInput);

        assertThat(result.getResult()).isEqualByComparingTo("79.54");
    }

    @Test
    @DisplayName("should calculate with high percentage correctly")
    void shouldCalculateWithHighPercentageCorrectly() {
        Calculation input = Calculation.builder()
                .num1(new BigDecimal("100.00"))
                .num2(new BigDecimal("100.00"))
                .build();

        BigDecimal percentage = new BigDecimal("50.00");
        when(percentageService.getPercentage()).thenReturn(percentage);

        Calculation result = calculatorService.execute(input);

        assertThat(result.getResult()).isEqualByComparingTo("300.00");
    }

    @Test
    @DisplayName("should calculate with zero percentage correctly")
    void shouldCalculateWithZeroPercentageCorrectly() {
        BigDecimal percentage = BigDecimal.ZERO;
        when(percentageService.getPercentage()).thenReturn(percentage);

        Calculation result = calculatorService.execute(inputCalculation);

        assertThat(result.getResult()).isEqualByComparingTo("300.00");
    }

    @Test
    @DisplayName("should calculate with maximum percentage correctly")
    void shouldCalculateWithMaximumPercentageCorrectly() {
        BigDecimal percentage = new BigDecimal("100.00");
        when(percentageService.getPercentage()).thenReturn(percentage);

        Calculation result = calculatorService.execute(inputCalculation);

        assertThat(result.getResult()).isEqualByComparingTo("600.00");
    }

    @Test
    @DisplayName("should apply HALF_UP rounding correctly")
    void shouldApplyHalfUpRoundingCorrectly() {
        Calculation input = Calculation.builder()
                .num1(new BigDecimal("10.00"))
                .num2(new BigDecimal("10.00"))
                .build();

        BigDecimal percentage = new BigDecimal("33.33");
        when(percentageService.getPercentage()).thenReturn(percentage);

        Calculation result = calculatorService.execute(input);

        assertThat(result.getResult()).isEqualByComparingTo("26.67");
    }

    @Test
    @DisplayName("should handle large numbers correctly")
    void shouldHandleLargeNumbersCorrectly() {
        Calculation largeInput = Calculation.builder()
                .num1(new BigDecimal("9999999999.99"))
                .num2(new BigDecimal("9999999999.99"))
                .build();

        BigDecimal percentage = new BigDecimal("10.00");
        when(percentageService.getPercentage()).thenReturn(percentage);

        Calculation result = calculatorService.execute(largeInput);

        assertThat(result.getResult()).isEqualByComparingTo("21999999999.98");
    }

    @Test
    @DisplayName("should publish calculation completed event")
    void shouldPublishCalculationCompletedEvent() {
        BigDecimal percentage = new BigDecimal("10.00");
        when(percentageService.getPercentage()).thenReturn(percentage);

        calculatorService.execute(inputCalculation);

        verify(eventPublisher).publishEvent(any(CalculationCompletedEvent.class));
    }

    @Test
    @DisplayName("should publish event with correct calculation data")
    void shouldPublishEventWithCorrectCalculationData() {
        BigDecimal percentage = new BigDecimal("10.00");
        when(percentageService.getPercentage()).thenReturn(percentage);

        Calculation result = calculatorService.execute(inputCalculation);

        verify(eventPublisher).publishEvent(eventCaptor.capture());
        CalculationCompletedEvent capturedEvent = eventCaptor.getValue();

        assertThat(capturedEvent.calculation()).isEqualTo(result);
        assertThat(capturedEvent.calculation().getResult()).isEqualByComparingTo("330.00");
    }

    @Test
    @DisplayName("should generate timestamp for calculation")
    void shouldGenerateTimestampForCalculation() {
        BigDecimal percentage = new BigDecimal("10.00");
        when(percentageService.getPercentage()).thenReturn(percentage);

        LocalDateTime before = LocalDateTime.now();
        Calculation result = calculatorService.execute(inputCalculation);
        LocalDateTime after = LocalDateTime.now();

        assertThat(result.getTimestamp())
                .isNotNull()
                .isAfterOrEqualTo(before)
                .isBeforeOrEqualTo(after);
    }

    @Test
    @DisplayName("should retrieve percentage from service")
    void shouldRetrievePercentageFromService() {
        BigDecimal percentage = new BigDecimal("10.00");
        when(percentageService.getPercentage()).thenReturn(percentage);

        calculatorService.execute(inputCalculation);

        verify(percentageService).getPercentage();
    }

    @Test
    @DisplayName("should preserve input values in result")
    void shouldPreserveInputValuesInResult() {
        BigDecimal percentage = new BigDecimal("10.00");
        when(percentageService.getPercentage()).thenReturn(percentage);

        Calculation result = calculatorService.execute(inputCalculation);

        assertThat(result.getNum1()).isEqualTo(inputCalculation.getNum1());
        assertThat(result.getNum2()).isEqualTo(inputCalculation.getNum2());
    }

    @Test
    @DisplayName("should calculate with fractional percentage correctly")
    void shouldCalculateWithFractionalPercentageCorrectly() {
        Calculation input = Calculation.builder()
                .num1(new BigDecimal("100.00"))
                .num2(new BigDecimal("50.00"))
                .build();

        BigDecimal percentage = new BigDecimal("7.50");
        when(percentageService.getPercentage()).thenReturn(percentage);

        Calculation result = calculatorService.execute(input);

        assertThat(result.getResult()).isEqualByComparingTo("161.25");
    }
}
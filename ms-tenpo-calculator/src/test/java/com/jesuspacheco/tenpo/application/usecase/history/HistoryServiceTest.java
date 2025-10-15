package com.jesuspacheco.tenpo.application.usecase.history;

import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import com.jesuspacheco.tenpo.domain.model.ResponseType;
import com.jesuspacheco.tenpo.domain.port.out.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@DisplayName("HistoryService")
class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryService historyService;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private List<CalculationHistory> sampleHistory;

    @BeforeEach
    void setUp() {
        sampleHistory = List.of(
                createHistory(1L, LocalDateTime.now()),
                createHistory(2L, LocalDateTime.now().minusMinutes(5)),
                createHistory(3L, LocalDateTime.now().minusMinutes(10))
        );
    }

    @Test
    @DisplayName("should return paginated history successfully")
    void shouldReturnPaginatedHistorySuccessfully() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<CalculationHistory> result = historyService.execute(0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent()).isEqualTo(sampleHistory);
    }

    @Test
    @DisplayName("should apply correct pagination parameters")
    void shouldApplyCorrectPaginationParameters() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        historyService.execute(2, 15);

        verify(historyRepository).findAll(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getPageNumber()).isEqualTo(2);
        assertThat(capturedPageable.getPageSize()).isEqualTo(15);
    }

    @Test
    @DisplayName("should sort by executedAt descending")
    void shouldSortByExecutedAtDescending() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        historyService.execute(0, 10);

        verify(historyRepository).findAll(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getSort().getOrderFor("executedAt"))
                .isNotNull()
                .extracting(Sort.Order::getDirection)
                .isEqualTo(Sort.Direction.DESC);
    }

    @Test
    @DisplayName("should clamp page to minimum value of 0")
    void shouldClampPageToMinimumValueOf0() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        historyService.execute(-5, 10);

        verify(historyRepository).findAll(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getPageNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("should clamp page to maximum value of 1000")
    void shouldClampPageToMaximumValueOf1000() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        historyService.execute(5000, 10);

        verify(historyRepository).findAll(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getPageNumber()).isEqualTo(1000);
    }

    @Test
    @DisplayName("should clamp size to minimum value of 1")
    void shouldClampSizeToMinimumValueOf1() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        historyService.execute(0, 0);

        verify(historyRepository).findAll(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getPageSize()).isEqualTo(1);
    }

    @Test
    @DisplayName("should clamp size to maximum value of 100")
    void shouldClampSizeToMaximumValueOf100() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        historyService.execute(0, 500);

        verify(historyRepository).findAll(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getPageSize()).isEqualTo(100);
    }

    @Test
    @DisplayName("should clamp negative size to minimum value")
    void shouldClampNegativeSizeToMinimumValue() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        historyService.execute(0, -10);

        verify(historyRepository).findAll(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getPageSize()).isEqualTo(1);
    }

    @Test
    @DisplayName("should handle empty result page")
    void shouldHandleEmptyResultPage() {
        Page<CalculationHistory> emptyPage = new PageImpl<>(List.of());
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<CalculationHistory> result = historyService.execute(0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("should throw exception when repository fails")
    void shouldThrowExceptionWhenRepositoryFails() {
        when(historyRepository.findAll(any(Pageable.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        assertThatThrownBy(() -> historyService.execute(0, 10))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unable to retrieve history");
    }

    @Test
    @DisplayName("should handle boundary page values correctly")
    void shouldHandleBoundaryPageValuesCorrectly() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        historyService.execute(1000, 10);

        verify(historyRepository).findAll(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getPageNumber()).isEqualTo(1000);
    }

    @Test
    @DisplayName("should handle boundary size values correctly")
    void shouldHandleBoundarySizeValuesCorrectly() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        historyService.execute(0, 100);

        verify(historyRepository).findAll(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getPageSize()).isEqualTo(100);
    }

    @Test
    @DisplayName("should call repository exactly once")
    void shouldCallRepositoryExactlyOnce() {
        Page<CalculationHistory> expectedPage = new PageImpl<>(sampleHistory);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        historyService.execute(0, 10);

        verify(historyRepository, times(1)).findAll(any(Pageable.class));
    }

    private CalculationHistory createHistory(Long id, LocalDateTime executedAt) {
        return CalculationHistory.builder()
                .id(id)
                .endpoint("/api/v1/calculator/sum")
                .method("POST")
                .status(200)
                .responseType(ResponseType.SUCCESS)
                .requestParams(Map.of("num1", 100, "num2", 200))
                .outcomeJson(Map.of("result", "330.00"))
                .executedAt(executedAt)
                .createdAt(LocalDateTime.now())
                .build();
    }
}

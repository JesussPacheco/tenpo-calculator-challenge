package com.jesuspacheco.tenpo.application.percentage;

import com.jesuspacheco.tenpo.domain.constant.CacheKeys;
import com.jesuspacheco.tenpo.domain.exception.ExternalServiceException;
import com.jesuspacheco.tenpo.domain.exception.PercentageUnavailableException;
import com.jesuspacheco.tenpo.domain.port.out.CacheProvider;
import com.jesuspacheco.tenpo.domain.port.out.PercentageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.reset;
@ExtendWith(MockitoExtension.class)
@DisplayName("PercentageService")
class PercentageServiceTest {

    private static final BigDecimal VALID_PERCENTAGE = new BigDecimal("10.50");
    private static final BigDecimal MIN_PERCENTAGE = BigDecimal.ZERO;
    private static final BigDecimal MAX_PERCENTAGE = new BigDecimal("100");
    @Mock
    private PercentageProvider percentageProvider;
    @Mock
    private CacheProvider cacheProvider;
    @InjectMocks
    private PercentageService percentageService;

    @BeforeEach
    void setUp() {
        reset(percentageProvider, cacheProvider);
    }

    @Test
    @DisplayName("should fetch percentage from provider successfully")
    void shouldFetchPercentageFromProviderSuccessfully() {
        when(percentageProvider.getPercentage()).thenReturn(VALID_PERCENTAGE);

        BigDecimal result = percentageService.getPercentage();

        assertThat(result).isEqualTo(VALID_PERCENTAGE);
        verify(percentageProvider).getPercentage();
    }

    @Test
    @DisplayName("should cache percentage after fetching from provider")
    void shouldCachePercentageAfterFetchingFromProvider() {
        when(percentageProvider.getPercentage()).thenReturn(VALID_PERCENTAGE);

        percentageService.getPercentage();

        verify(cacheProvider).put(CacheKeys.PERCENTAGE_LATEST, VALID_PERCENTAGE);
    }

    @Test
    @DisplayName("should use cached percentage when provider fails")
    void shouldUseCachedPercentageWhenProviderFails() {
        when(percentageProvider.getPercentage()).thenThrow(new RuntimeException("Service unavailable"));
        when(cacheProvider.get(CacheKeys.PERCENTAGE_LATEST)).thenReturn(Optional.of(VALID_PERCENTAGE));

        BigDecimal result = percentageService.getPercentage();

        assertThat(result).isEqualTo(VALID_PERCENTAGE);
        verify(cacheProvider).get(CacheKeys.PERCENTAGE_LATEST);
        verify(cacheProvider, never()).put(any(), any());
    }

    @Test
    @DisplayName("should throw exception when provider fails and no cache available")
    void shouldThrowExceptionWhenProviderFailsAndNoCacheAvailable() {
        when(percentageProvider.getPercentage()).thenThrow(new ExternalServiceException("Service unavailable"));
        when(cacheProvider.get(CacheKeys.PERCENTAGE_LATEST)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> percentageService.getPercentage())
                .isInstanceOf(PercentageUnavailableException.class)
                .hasMessageContaining("No cached value");

        verify(cacheProvider).get(CacheKeys.PERCENTAGE_LATEST);
    }

    @Test
    @DisplayName("should accept minimum valid percentage")
    void shouldAcceptMinimumValidPercentage() {
        when(percentageProvider.getPercentage()).thenReturn(MIN_PERCENTAGE);

        BigDecimal result = percentageService.getPercentage();

        assertThat(result).isEqualTo(MIN_PERCENTAGE);
        verify(cacheProvider).put(CacheKeys.PERCENTAGE_LATEST, MIN_PERCENTAGE);
    }

    @Test
    @DisplayName("should accept maximum valid percentage")
    void shouldAcceptMaximumValidPercentage() {
        when(percentageProvider.getPercentage()).thenReturn(MAX_PERCENTAGE);

        BigDecimal result = percentageService.getPercentage();

        assertThat(result).isEqualTo(MAX_PERCENTAGE);
        verify(cacheProvider).put(CacheKeys.PERCENTAGE_LATEST, MAX_PERCENTAGE);
    }

    @Test
    @DisplayName("should use cache when provider returns null")
    void shouldUseCacheWhenProviderReturnsNull() {
        when(percentageProvider.getPercentage()).thenReturn(null);
        when(cacheProvider.get(CacheKeys.PERCENTAGE_LATEST)).thenReturn(Optional.of(VALID_PERCENTAGE));

        BigDecimal result = percentageService.getPercentage();

        assertThat(result).isEqualTo(VALID_PERCENTAGE);
        verify(cacheProvider, never()).put(any(), any());
    }

    @Test
    @DisplayName("should use cache when provider returns negative percentage")
    void shouldUseCacheWhenProviderReturnsNegativePercentage() {
        BigDecimal negativePercentage = new BigDecimal("-5.00");
        when(percentageProvider.getPercentage()).thenReturn(negativePercentage);
        when(cacheProvider.get(CacheKeys.PERCENTAGE_LATEST)).thenReturn(Optional.of(VALID_PERCENTAGE));

        BigDecimal result = percentageService.getPercentage();

        assertThat(result).isEqualTo(VALID_PERCENTAGE);
        verify(cacheProvider, never()).put(eq(CacheKeys.PERCENTAGE_LATEST), eq(negativePercentage));
    }

    @Test
    @DisplayName("should use cache when provider returns percentage above 100")
    void shouldUseCacheWhenProviderReturnsPercentageAbove100() {
        BigDecimal excessivePercentage = new BigDecimal("150.00");
        when(percentageProvider.getPercentage()).thenReturn(excessivePercentage);
        when(cacheProvider.get(CacheKeys.PERCENTAGE_LATEST)).thenReturn(Optional.of(VALID_PERCENTAGE));

        BigDecimal result = percentageService.getPercentage();

        assertThat(result).isEqualTo(VALID_PERCENTAGE);
        verify(cacheProvider, never()).put(eq(CacheKeys.PERCENTAGE_LATEST), eq(excessivePercentage));
    }

    @Test
    @DisplayName("should throw exception when invalid percentage and no cache")
    void shouldThrowExceptionWhenInvalidPercentageAndNoCache() {
        BigDecimal invalidPercentage = new BigDecimal("-10.00");
        when(percentageProvider.getPercentage()).thenReturn(invalidPercentage);
        when(cacheProvider.get(CacheKeys.PERCENTAGE_LATEST)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> percentageService.getPercentage())
                .isInstanceOf(PercentageUnavailableException.class);
    }

    @Test
    @DisplayName("should not cache invalid percentage values")
    void shouldNotCacheInvalidPercentageValues() {
        BigDecimal invalidPercentage = new BigDecimal("200.00");
        when(percentageProvider.getPercentage()).thenReturn(invalidPercentage);
        when(cacheProvider.get(CacheKeys.PERCENTAGE_LATEST)).thenReturn(Optional.of(VALID_PERCENTAGE));

        percentageService.getPercentage();

        verify(cacheProvider, never()).put(eq(CacheKeys.PERCENTAGE_LATEST), eq(invalidPercentage));
    }

    @Test
    @DisplayName("should handle decimal percentages correctly")
    void shouldHandleDecimalPercentagesCorrectly() {
        BigDecimal decimalPercentage = new BigDecimal("15.75");
        when(percentageProvider.getPercentage()).thenReturn(decimalPercentage);

        BigDecimal result = percentageService.getPercentage();

        assertThat(result)
                .isEqualByComparingTo(decimalPercentage)
                .isBetween(MIN_PERCENTAGE, MAX_PERCENTAGE);
        verify(cacheProvider).put(CacheKeys.PERCENTAGE_LATEST, decimalPercentage);
    }
}

package com.jesuspacheco.tenpo.application.usecase.history;

import com.jesuspacheco.tenpo.application.usecase.calculation.event.CalculationCompletedEvent;
import com.jesuspacheco.tenpo.application.usecase.history.mapper.ApiErrorMapper;
import com.jesuspacheco.tenpo.application.usecase.history.mapper.CalculationHistoryMapper;
import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import com.jesuspacheco.tenpo.domain.port.out.HistoryRepository;
import com.jesuspacheco.tenpo.infrastructure.constant.ApiConstants;
import com.jesuspacheco.tenpo.infrastructure.filter.event.ApiErrorCalculationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener for calculation events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HistoryEventListener {

    private final HistoryRepository historyRepository;
    private final CalculationHistoryMapper successMapper;
    private final ApiErrorMapper errorMapper;

    @Async
    @EventListener
    public void onCalculationCompleted(CalculationCompletedEvent event) {
        CalculationHistory history = successMapper.toSuccessHistory(
                event.calculation(),
                ApiConstants.CALCULATOR_ENDPOINT,
                ApiConstants.HTTP_METHOD_POST
        );

        persistHistory(history);
    }

    @Async
    @EventListener
    public void onApiError(ApiErrorCalculationEvent event) {
        CalculationHistory history = errorMapper.toHistory(event);
        persistHistory(history);
    }

    private void persistHistory(CalculationHistory history) {
        try {
            historyRepository.save(history);
            log.trace("History persisted: {} {}", history.getMethod(), history.getEndpoint());
        } catch (Exception e) {
            log.error("Failed to persist history", e);
        }
    }
}

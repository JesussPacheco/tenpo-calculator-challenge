package com.jesuspacheco.tenpo.application.usecase.history;

import com.jesuspacheco.tenpo.domain.exception.HistoryUnavailableException;
import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import com.jesuspacheco.tenpo.domain.port.in.GetHistoryUseCase;
import com.jesuspacheco.tenpo.domain.port.out.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Service responsible for retrieving calculation history.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService implements GetHistoryUseCase {

    private final HistoryRepository historyRepository;

    @Override
    public Page<CalculationHistory> execute(int page, int size) {
        final int safePage = Math.clamp(page, 0, 1000);
        final int safeSize = Math.clamp(size, 1, 100);

        try {
            Pageable pageable = createPageable(safePage, safeSize);
            return historyRepository.findAll(pageable);
        } catch (Exception ex) {
            log.error("Failed to retrieve history: {}", ex.getMessage());
            throw new HistoryUnavailableException("Unable to retrieve history at this time");
        }
    }

    private Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by("executedAt").descending());
    }
}

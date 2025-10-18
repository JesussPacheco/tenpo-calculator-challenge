package com.jesuspacheco.tenpo.domain.port.out;

import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository for calculation history persistence.
 */
public interface HistoryRepository {

    /**
     * Saves calculation history asynchronously.
     * Must not block the main request flow.
     *
     * @param history history entry to save
     */
    CalculationHistory save(CalculationHistory history);

    /**
     * Retrieves paginated calculation history.
     *
     * @param pageable pagination parameters
     * @return paginated history entries
     */
    Page<CalculationHistory> findAll(Pageable pageable);
}
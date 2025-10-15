package com.jesuspacheco.tenpo.domain.port.in;

import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import org.springframework.data.domain.Page;

/**
 * Use case for retrieving calculation history.
 */
public interface GetHistoryUseCase {

    /**
     * Retrieves paginated calculation history.
     *
     * @param page page number (0-indexed)
     * @param size page size
     * @return paginated history entries
     */
    Page<CalculationHistory> execute(int page, int size);
}
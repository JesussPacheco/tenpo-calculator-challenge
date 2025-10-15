package com.jesuspacheco.tenpo.application.usecase.history.mapper;

import com.jesuspacheco.tenpo.domain.model.Calculation;
import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import com.jesuspacheco.tenpo.domain.model.ResponseType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Mapper for transforming domain objects to history entries.
 */
@Component
public class CalculationHistoryMapper {
    private static final Integer SUCCESS_STATUS = 200;

    public CalculationHistory toSuccessHistory(
            Calculation calculation,
            String endpoint,
            String method
    ) {
        return CalculationHistory.builder()
                .endpoint(endpoint)
                .method(method)
                .status(SUCCESS_STATUS)
                .responseType(ResponseType.SUCCESS)
                .requestParams(buildRequestParams(calculation))
                .outcomeJson(buildSuccessOutcome(calculation))
                .executedAt(calculation.getTimestamp())
                .build();
    }


    private Map<String, Object> buildRequestParams(Calculation calculation) {
        return Map.of(
                "num1", calculation.getNum1(),
                "num2", calculation.getNum2()
        );
    }

    private Map<String, Object> buildSuccessOutcome(Calculation calculation) {
        return Map.of(
                "num1", calculation.getNum1(),
                "num2", calculation.getNum2(),
                "percentage", calculation.getPercentage().toString(),
                "result", calculation.getResult().toString(),
                "timestamp", calculation.getTimestamp().toString()
        );
    }
}

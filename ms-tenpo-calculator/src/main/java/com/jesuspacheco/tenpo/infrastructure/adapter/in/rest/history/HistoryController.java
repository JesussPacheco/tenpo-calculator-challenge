package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.history;

import com.jesuspacheco.tenpo.domain.port.in.GetHistoryUseCase;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.common.dto.PageResponse;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.history.docs.HistoryApiDoc;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.history.dto.HistoryDtoMapper;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.history.dto.HistoryResponse;
import com.jesuspacheco.tenpo.infrastructure.constant.ApiConstants;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for history operations.
 */
@RestController
@RequestMapping(ApiConstants.HISTORY_BASE_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "History", description = "Calculation history operations")
public class HistoryController {

    private final GetHistoryUseCase getHistoryUseCase;
    private final HistoryDtoMapper historyDtoMapper;

    @GetMapping
    @HistoryApiDoc
    public ResponseEntity<PageResponse<HistoryResponse>> getHistory(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Fetching history page");

        Page<HistoryResponse> historyPage = getHistoryUseCase.execute(page, size)
                .map(historyDtoMapper::toResponse);

        return ResponseEntity.ok(PageResponse.of(historyPage));
    }
}
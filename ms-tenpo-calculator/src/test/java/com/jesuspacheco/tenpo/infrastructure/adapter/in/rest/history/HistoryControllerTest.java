package com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.history;

import com.jesuspacheco.tenpo.domain.model.CalculationHistory;
import com.jesuspacheco.tenpo.domain.model.ResponseType;
import com.jesuspacheco.tenpo.domain.port.in.GetHistoryUseCase;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.history.dto.HistoryDtoMapper;
import com.jesuspacheco.tenpo.infrastructure.adapter.in.rest.history.dto.HistoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoryController.class)
@DisplayName("HistoryController")
class HistoryControllerTest {

    private static final String HISTORY_ENDPOINT = "/api/v1/history";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GetHistoryUseCase getHistoryUseCase;
    @MockBean
    private HistoryDtoMapper historyDtoMapper;

    @Test
    @DisplayName("should return 200 OK with paginated history")
    void shouldReturn200OkWithPaginatedHistory() throws Exception {
        List<CalculationHistory> historyList = List.of(
                createHistory(1L),
                createHistory(2L),
                createHistory(3L)
        );
        Page<CalculationHistory> historyPage = new PageImpl<>(
                historyList,
                PageRequest.of(0, 10),
                historyList.size()
        );
        List<HistoryResponse> responseList = historyList.stream()
                .map(this::createHistoryResponse)
                .toList();

        when(getHistoryUseCase.execute(0, 10)).thenReturn(historyPage);
        when(historyDtoMapper.toResponse(historyList.get(0))).thenReturn(responseList.get(0));
        when(historyDtoMapper.toResponse(historyList.get(1))).thenReturn(responseList.get(1));
        when(historyDtoMapper.toResponse(historyList.get(2))).thenReturn(responseList.get(2));

        mockMvc.perform(get(HISTORY_ENDPOINT)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    @DisplayName("should use default pagination parameters when not provided")
    void shouldUseDefaultPaginationParametersWhenNotProvided() throws Exception {
        Page<CalculationHistory> emptyPage = new PageImpl<>(
                List.of(),
                PageRequest.of(0, 10),
                0
        );
        when(getHistoryUseCase.execute(0, 10)).thenReturn(emptyPage);

        mockMvc.perform(get(HISTORY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    @DisplayName("should return PageResponse structure with all fields")
    void shouldReturnPageResponseStructureWithAllFields() throws Exception {
        List<CalculationHistory> historyList = List.of(createHistory(1L));
        Page<CalculationHistory> historyPage = new PageImpl<>(historyList);
        HistoryResponse response = createHistoryResponse(historyList.get(0));

        when(getHistoryUseCase.execute(anyInt(), anyInt())).thenReturn(historyPage);
        when(historyDtoMapper.toResponse(historyList.get(0))).thenReturn(response);

        mockMvc.perform(get(HISTORY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.first").exists())
                .andExpect(jsonPath("$.last").exists());
    }

    @Test
    @DisplayName("should return empty content when no history available")
    void shouldReturnEmptyContentWhenNoHistoryAvailable() throws Exception {
        Page<CalculationHistory> emptyPage = new PageImpl<>(List.of());
        when(getHistoryUseCase.execute(anyInt(), anyInt())).thenReturn(emptyPage);

        mockMvc.perform(get(HISTORY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("should handle custom page and size parameters")
    void shouldHandleCustomPageAndSizeParameters() throws Exception {
        List<CalculationHistory> historyList = List.of(createHistory(1L));
        Page<CalculationHistory> historyPage = new PageImpl<>(historyList);
        HistoryResponse response = createHistoryResponse(historyList.get(0));

        when(getHistoryUseCase.execute(2, 20)).thenReturn(historyPage);
        when(historyDtoMapper.toResponse(historyList.get(0))).thenReturn(response);

        mockMvc.perform(get(HISTORY_ENDPOINT)
                        .param("page", "2")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should return history entry with all expected fields")
    void shouldReturnHistoryEntryWithAllExpectedFields() throws Exception {
        CalculationHistory history = createHistory(1L);
        HistoryResponse response = createHistoryResponse(history);
        Page<CalculationHistory> historyPage = new PageImpl<>(List.of(history));

        when(getHistoryUseCase.execute(anyInt(), anyInt())).thenReturn(historyPage);
        when(historyDtoMapper.toResponse(history)).thenReturn(response);

        mockMvc.perform(get(HISTORY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].endpoint").value("/api/v1/calculator/sum"))
                .andExpect(jsonPath("$.content[0].method").value("POST"))
                .andExpect(jsonPath("$.content[0].status").value(200))
                .andExpect(jsonPath("$.content[0].responseType").value("SUCCESS"))
                .andExpect(jsonPath("$.content[0].requestParams").exists())
                .andExpect(jsonPath("$.content[0].outcomeJson").exists())
                .andExpect(jsonPath("$.content[0].executedAt").exists());
    }


    @Test
    @DisplayName("should return first and last page flags correctly")
    void shouldReturnFirstAndLastPageFlagsCorrectly() throws Exception {
        List<CalculationHistory> historyList = List.of(createHistory(1L));
        Page<CalculationHistory> historyPage = new PageImpl<>(historyList);
        HistoryResponse response = createHistoryResponse(historyList.get(0));

        when(getHistoryUseCase.execute(anyInt(), anyInt())).thenReturn(historyPage);
        when(historyDtoMapper.toResponse(historyList.get(0))).thenReturn(response);

        mockMvc.perform(get(HISTORY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true));
    }

    @ParameterizedTest(name = "should handle page={0}, size={1} gracefully")
    @CsvSource({
            "999, 10",    // Large page number
            "0, 100",     // Maximum size
            "-1, 10",     // Negative page
            "0, 0"        // Zero size
    })
    @DisplayName("should handle various pagination parameters")
    void shouldHandleVariousPaginationParameters(int page, int size) throws Exception {
        Page<CalculationHistory> emptyPage = new PageImpl<>(List.of());
        when(getHistoryUseCase.execute(anyInt(), anyInt())).thenReturn(emptyPage);

        mockMvc.perform(get(HISTORY_ENDPOINT)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should return 500 when service throws unexpected exception")
    void shouldReturn500WhenServiceThrowsUnexpectedException() throws Exception {
        when(getHistoryUseCase.execute(anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get(HISTORY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("GENERIC_ERROR"));
    }

    private CalculationHistory createHistory(Long id) {
        return CalculationHistory.builder()
                .id(id)
                .endpoint("/api/v1/calculator/sum")
                .method("POST")
                .status(200)
                .responseType(ResponseType.SUCCESS)
                .requestParams(Map.of("num1", 100, "num2", 200))
                .outcomeJson(Map.of("result", "330.00"))
                .executedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private HistoryResponse createHistoryResponse(CalculationHistory history) {
        return new HistoryResponse(
                history.getId(),
                history.getEndpoint(),
                history.getMethod(),
                history.getStatus(),
                history.getResponseType().getCode(),
                history.getRequestParams(),
                history.getOutcomeJson(),
                history.getExecutedAt()
        );
    }
}

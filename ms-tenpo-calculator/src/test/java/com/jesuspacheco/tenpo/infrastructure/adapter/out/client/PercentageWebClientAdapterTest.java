package com.jesuspacheco.tenpo.infrastructure.adapter.out.client;

import com.jesuspacheco.tenpo.domain.exception.ExternalServiceException;
import com.jesuspacheco.tenpo.infrastructure.config.client.PercentageClientProperties;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PercentageWebClientAdapter")
class PercentageWebClientAdapterTest {

    private MockWebServer mockWebServer;
    private PercentageWebClientAdapter adapter;
    private PercentageClientProperties properties;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        properties = new PercentageClientProperties();
        properties.setUrl(mockWebServer.url("/").toString());
        properties.setTimeout(Duration.ofSeconds(5));

        WebClient webClient = WebClient.builder()
                .baseUrl(properties.getUrl())
                .build();

        adapter = new PercentageWebClientAdapter(webClient, properties);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("should fetch percentage successfully")
    void shouldFetchPercentageSuccessfully() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"percentage\": 10.50}")
                .addHeader("Content-Type", "application/json"));

        BigDecimal result = adapter.getPercentage();

        assertThat(result).isEqualByComparingTo("10.50");
    }

    @Test
    @DisplayName("should handle decimal percentages correctly")
    void shouldHandleDecimalPercentagesCorrectly() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"percentage\": 15.75}")
                .addHeader("Content-Type", "application/json"));

        BigDecimal result = adapter.getPercentage();

        assertThat(result).isEqualByComparingTo("15.75");
    }

    @Test
    @DisplayName("should handle zero percentage")
    void shouldHandleZeroPercentage() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"percentage\": 0.00}")
                .addHeader("Content-Type", "application/json"));

        BigDecimal result = adapter.getPercentage();

        assertThat(result).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("should handle maximum percentage")
    void shouldHandleMaximumPercentage() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"percentage\": 100.00}")
                .addHeader("Content-Type", "application/json"));

        BigDecimal result = adapter.getPercentage();

        assertThat(result).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("should throw exception when service returns 500")
    void shouldThrowExceptionWhenServiceReturns500() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        assertThatThrownBy(() -> adapter.getPercentage())
                .isInstanceOf(ExternalServiceException.class);
    }

    @Test
    @DisplayName("should throw exception when service returns 503")
    void shouldThrowExceptionWhenServiceReturns503() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(503)
                .setBody("Service Unavailable"));

        assertThatThrownBy(() -> adapter.getPercentage())
                .isInstanceOf(ExternalServiceException.class);
    }

    @Test
    @DisplayName("should throw exception when response is invalid JSON")
    void shouldThrowExceptionWhenResponseIsInvalidJson() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("invalid json")
                .addHeader("Content-Type", "application/json"));

        assertThatThrownBy(() -> adapter.getPercentage())
                .isInstanceOf(ExternalServiceException.class);
    }

    @Test
    @DisplayName("should throw exception when response is missing percentage field")
    void shouldThrowExceptionWhenResponseIsMissingPercentageField() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"otherField\": 10.50}")
                .addHeader("Content-Type", "application/json"));

        assertThatThrownBy(() -> adapter.getPercentage())
                .isInstanceOf(ExternalServiceException.class);
    }

    @Test
    @DisplayName("should throw exception when percentage is null")
    void shouldThrowExceptionWhenPercentageIsNull() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"percentage\": null}")
                .addHeader("Content-Type", "application/json"));

        assertThatThrownBy(() -> adapter.getPercentage())
                .isInstanceOf(ExternalServiceException.class);
    }

    @Test
    @DisplayName("should throw exception on network error")
    void shouldThrowExceptionOnNetworkError() throws IOException {
        mockWebServer.shutdown();

        assertThatThrownBy(() -> adapter.getPercentage())
                .isInstanceOf(ExternalServiceException.class);
    }
}
package com.jesuspacheco.tenpo.integration;

import com.jesuspacheco.tenpo.config.TestcontainersConfiguration;
import com.jesuspacheco.tenpo.domain.port.out.PercentageProvider;
import com.jesuspacheco.tenpo.infrastructure.adapter.out.persistence.repository.HistoryJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Calculator Integration")
class CalculatorIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private HistoryJpaRepository historyJpaRepository;

    @MockBean
    private PercentageProvider percentageProvider;

    @BeforeEach
    void setUp() {
        historyJpaRepository.deleteAll();
        when(percentageProvider.getPercentage()).thenReturn(new BigDecimal("10.00"));
    }

    @Test
    @DisplayName("should calculate and persist to database")
    void shouldCalculateAndPersistToDatabase() {
        Map<String, Object> request = Map.of(
                "num1", 100.00,
                "num2", 200.00
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/calculator/sum",
                request,
                Map.class
        );

        await().atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    long count = historyJpaRepository.count();
                    assertThat(count).isEqualTo(1);
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("num1")).isEqualTo(100.0);
        assertThat(response.getBody().get("num2")).isEqualTo(200.0);
        assertThat(response.getBody().get("percentage")).isEqualTo(10.0);
        assertThat(response.getBody().get("result")).isEqualTo(330.0);
        assertThat(response.getBody().get("timestamp")).isNotNull();


    }

    @Test
    @DisplayName("should retrieve history from database")
    void shouldRetrieveHistoryFromDatabase() {
        Map<String, Object> calculationRequest = Map.of(
                "num1", 50.00,
                "num2", 75.00
        );

        restTemplate.postForEntity("/api/v1/calculator/sum", calculationRequest, Map.class);
        restTemplate.postForEntity("/api/v1/calculator/sum", calculationRequest, Map.class);

        await().atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    long count = historyJpaRepository.count();
                    assertThat(count).isEqualTo(2);
                });

        ResponseEntity<Map> historyResponse = restTemplate.getForEntity(
                "/api/v1/history?page=0&size=10",
                Map.class
        );

        assertThat(historyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(historyResponse.getBody()).isNotNull();
        assertThat(historyResponse.getBody().get("totalElements")).isEqualTo(2);


    }

}
package com.jesuspacheco.tenpo.infrastructure.adapter.out.client;

import com.jesuspacheco.tenpo.domain.exception.ExternalServiceException;
import com.jesuspacheco.tenpo.domain.port.out.PercentageProvider;
import com.jesuspacheco.tenpo.infrastructure.adapter.out.client.dto.PercentageResponse;
import com.jesuspacheco.tenpo.infrastructure.config.client.PercentageClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;

/**
 * Adapter that implements PercentageProvider port using WebClient.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PercentageWebClientAdapter implements PercentageProvider {

    private static final String GET_PERCENTAGE_ENDPOINT = "/api/percentage";
    private final WebClient percentageWebClient;
    private final PercentageClientProperties percentageClientProperties;

    @Override
    public BigDecimal getPercentage() {
        log.debug("Requesting percentage from external service");
        return percentageWebClient
                .get()
                .uri(GET_PERCENTAGE_ENDPOINT)
                .retrieve()
                .bodyToMono(PercentageResponse.class)
                .timeout(percentageClientProperties.getTimeout())
                .map(PercentageResponse::percentage)
                .doOnSuccess(percentage ->
                        log.debug("Received percentage={}", percentage))
                .doOnError(error ->
                        log.error("Error cause={}", error.getMessage()))
                .onErrorMap(this::mapToBusinessException)
                .block();
    }

    private ExternalServiceException mapToBusinessException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException webClientException) {
            return new ExternalServiceException(
                    String.format("External service returned status %d: %s",
                            webClientException.getStatusCode().value(),
                            webClientException.getResponseBodyAsString())
            );
        }
        return new ExternalServiceException(
                String.format("failed to fetch percentage: %s", throwable.getMessage())
        );
    }
}
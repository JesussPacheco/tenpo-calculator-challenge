package com.jesuspacheco.tenpo.infrastructure.config.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Configuration for WebClient to communicate with external services.
 */
@Configuration
@RequiredArgsConstructor
public class PercentageClientConfig {

    private final PercentageClientProperties percentageClientProperties;

    @Bean
    public WebClient percentageWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        (int) percentageClientProperties.getTimeout().toMillis())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(
                                percentageClientProperties.getTimeout().toSeconds(),
                                TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(
                                percentageClientProperties.getTimeout().toSeconds(),
                                TimeUnit.SECONDS)));

        return WebClient.builder()
                .baseUrl(percentageClientProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
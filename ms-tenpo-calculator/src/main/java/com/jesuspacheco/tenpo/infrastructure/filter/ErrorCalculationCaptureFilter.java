package com.jesuspacheco.tenpo.infrastructure.filter;

import com.jesuspacheco.tenpo.infrastructure.filter.event.ApiErrorCalculationEvent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class ErrorCalculationCaptureFilter extends OncePerRequestFilter {

    private static final String API_PATH_PREFIX = "/api/v1/calculator";
    private static final Integer STATUS_ERROR_THRESHOLD = 400;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith(API_PATH_PREFIX);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } catch (Exception e) {
            log.error("Filter caught exception", e);
        } finally {
            if (responseWrapper.getStatus() >= STATUS_ERROR_THRESHOLD) {
                publishErrorEvent(requestWrapper, responseWrapper);
            }
            responseWrapper.copyBodyToResponse();
        }
    }

    private void publishErrorEvent(ContentCachingRequestWrapper request,
                                   ContentCachingResponseWrapper response) {
        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);

        eventPublisher.publishEvent(new ApiErrorCalculationEvent(
                request.getRequestURI(),
                request.getMethod(),
                response.getStatus(),
                requestBody,
                responseBody,
                LocalDateTime.now()
        ));
    }
}
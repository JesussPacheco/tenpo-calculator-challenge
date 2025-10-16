package com.jesuspacheco.percentage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * External Percentage Service - Mock for testing.
 *
 * Returns a random percentage value between 5% and 15%.
 */
@SpringBootApplication
public class PercentageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PercentageServiceApplication.class, args);
    }
}
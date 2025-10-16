package com.jesuspacheco.tenpo;

import com.jesuspacheco.tenpo.config.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Application Context")
class TenpoCalculatorApplicationTests {

    @Test
    @DisplayName("should load application context with TestContainers")
    void contextLoads() {
    }
}
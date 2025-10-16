package com.jesuspacheco.tenpo.infrastructure.config.documentation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for OpenAPI documentation.
 */
@Component
@Getter
@Setter
public class OpenApiProperties {

    private String title = "Tenpo Calculator API";
    private String description = "API for calculating sums with dynamic percentage";
    private String version = "1.0.0";
    private String contactName = "Jesus Pacheco";
    private String contactEmail = "jesuspacheco0219@gmail.com";
}

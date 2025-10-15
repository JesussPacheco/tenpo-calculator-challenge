package com.jesuspacheco.tenpo.infrastructure.config.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for OpenAPI documentation (Swagger).
 */
@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final OpenApiProperties openApiProperties;

    @Bean
    public OpenAPI tenpoCalculatorApi() {
        Contact contact = new Contact()
                .name(openApiProperties.getContactName())
                .email(openApiProperties.getContactEmail());

        Info info = new Info()
                .title(openApiProperties.getTitle())
                .description(openApiProperties.getDescription())
                .version(openApiProperties.getVersion())
                .contact(contact);

        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local development server");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
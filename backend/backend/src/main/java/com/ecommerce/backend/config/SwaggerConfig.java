package com.ecommerce.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${app.swagger.contact-name:Admin}")
    private String contactName;

    @Value("${app.swagger.contact-email:admin@ecommerce.com}")
    private String contactEmail;

    @Value("${app.swagger.base-url:http://localhost:8080/api}")
    private String baseUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server()
                        .url(baseUrl)
                        .description("Servidor local"))
                .info(new Info()
                        .title("E-commerce API")
                        .version("1.0")
                        .description("API REST y GraphQL para sistema E-commerce")
                        .contact(new Contact()
                                .name(contactName)
                                .email(contactEmail)));
    }

    @Bean
    public GroupedOpenApi restApi() {
        return GroupedOpenApi.builder()
                .group("1-REST-API")
                // Cambiamos esto para que escanee TODOS los controladores bajo el paquete 'rest' e 'IA'
                .packagesToScan("com.ecommerce.backend.controller.rest", "com.ecommerce.backend.controller")
                .pathsToExclude("/graphql/**")
                .build();
    }

    @Bean
    public GroupedOpenApi graphqlApi() {
        return GroupedOpenApi.builder()
                .group("2-GRAPHQL-API")
                .pathsToMatch("/graphql/**")
                .build();
    }
}

package com.ecommerce.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la documentación de la API.
 * Permite la visualización interactiva de los endpoints REST y GraphQL.
 */
@Configuration
public class SwaggerConfig {

    @Value("${app.swagger.contact-name:Admin}")
    private String contactName;

    @Value("${app.swagger.contact-email:admin@ecommerce.com}")
    private String contactEmail;

    @Value("${app.swagger.base-url:http://localhost:8080/api}")
    private String baseUrl;

    /**
     * Configura la información general de la API para Swagger UI.
     *
     * @return Objeto OpenAPI con metadatos de la API.
     */
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

    /**
     * Define un grupo de APIs para los controladores REST.
     * Incluye controladores en 'com.ecommerce.backend.controller.rest' y 'com.ecommerce.backend.controller'.
     *
     * @return Objeto GroupedOpenApi para las APIs REST.
     */
    @Bean
    public GroupedOpenApi restApi() {
        return GroupedOpenApi.builder()
                .group("1-REST-API")
                .packagesToScan("com.ecommerce.backend.controller.rest", "com.ecommerce.backend.controller")
                .pathsToExclude("/graphql/**") // Excluye rutas GraphQL de este grupo
                .build();
    }

    /**
     * Define un grupo de APIs para los endpoints GraphQL.
     * Muestra el endpoint /graphql.
     *
     * @return Objeto GroupedOpenApi para las APIs GraphQL.
     */
    @Bean
    public GroupedOpenApi graphqlApi() {
        return GroupedOpenApi.builder()
                .group("2-GRAPHQL-API")
                .pathsToMatch("/graphql/**")
                .build();
    }
}

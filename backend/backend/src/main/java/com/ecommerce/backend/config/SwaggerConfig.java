package com.ecommerce.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server()
                        .url("http://localhost:8080/api")
                        .description("Servidor local"))
                .info(new Info()
                        .title("E-commerce API")
                        .version("1.0")
                        .description("API REST para sistema E-commerce")
                        .contact(new Contact()
                                .name("Jhoan Duque")
                                .email("jhoan.duque-c@uniminuto.edu.co")));
    }
}
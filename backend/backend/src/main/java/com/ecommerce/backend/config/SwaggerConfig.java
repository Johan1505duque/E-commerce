package com.ecommerce.backend.config;

import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce API")
                        .version("1.0")
                        .description("API REST para sistema E-commerce")
                        .contact(new Contact()
                                .name("Jhoan Duque")
                                .email("jhoan.duque-c@uniminuto.edu.co")));
    }
}
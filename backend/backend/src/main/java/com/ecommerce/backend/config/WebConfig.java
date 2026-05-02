package com.ecommerce.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración CORS (Cross-Origin Resource Sharing) para la aplicación.
 * Permite que el frontend de React acceda a los recursos del backend.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura las reglas CORS para permitir peticiones desde el frontend.
     *
     * @param registry El registro CORS para añadir mapeos.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica la configuración CORS a todos los endpoints
                .allowedOrigins("http://localhost:5173") // Permite peticiones desde el origen del frontend de React
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Métodos HTTP permitidos
                .allowedHeaders("*") // Permite todos los encabezados en las peticiones
                .allowCredentials(true); // Permite el envío de cookies de autenticación
    }
}

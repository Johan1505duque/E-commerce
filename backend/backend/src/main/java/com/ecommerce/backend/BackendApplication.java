package com.ecommerce.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class BackendApplication {

    private static final Logger log =
            LoggerFactory.getLogger(BackendApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(BackendApplication.class, args);
            log.info("✅ E-commerce API iniciada correctamente");
            log.info("📡 Corriendo en: http://localhost:8080/api");
        } catch (Exception e) {
            log.error("❌ Error al iniciar la aplicación: {}", e.getMessage());
        }
    }
}

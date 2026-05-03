package com.ecommerce.backend.config;

import com.ecommerce.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de seguridad para la aplicación Spring Boot.
 * Habilita la seguridad web, la seguridad a nivel de método y configura la autenticación JWT.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Define la cadena de filtros de seguridad HTTP.
     * Configura CSRF, gestión de sesiones, reglas de autorización y añade el filtro JWT.
     *
     * @param http Objeto HttpSecurity para configurar la seguridad.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF para APIs REST (JWT maneja la seguridad)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No usa sesiones HTTP
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas (sin autenticación)
                .requestMatchers("/auth/**").permitAll() // Endpoints de autenticación (login, registro)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/graphiql/**", "/graphql/**").permitAll() // Documentación y herramientas GraphQL
                .requestMatchers("/h2-console/**").permitAll() // Consola H2 (solo para desarrollo, considerar proteger en producción)
                .requestMatchers("/actuator/**").permitAll() // Permitir acceso a todos los endpoints de Actuator (para monitoreo)
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            );

        // Añade el filtro JWT antes del filtro de autenticación de usuario y contraseña estándar de Spring Security
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Proporciona un bean para el codificador de contraseñas.
     * Se utiliza BCryptPasswordEncoder para almacenar contraseñas de forma segura.
     *
     * @return Una instancia de PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expone el AuthenticationManager como un bean.
     * Necesario para el proceso de autenticación en el controlador de login.
     *
     * @param authenticationConfiguration Configuración de autenticación.
     * @return Una instancia de AuthenticationManager.
     * @throws Exception Si ocurre un error al obtener el AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

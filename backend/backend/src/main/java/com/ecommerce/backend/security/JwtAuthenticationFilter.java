package com.ecommerce.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService; // Usaremos nuestra implementación de UserDetailsService

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Verificar si el header Authorization existe y tiene el formato correcto
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer el token JWT
        jwt = authHeader.substring(7);

        userEmail = jwtUtil.extractUsername(jwt); // Extraer el email del token

        // 3. Si el email existe y el usuario no está ya autenticado
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Cargar los detalles del usuario
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 4. Validar el token
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                // Si el token es válido, crear un objeto de autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Las credenciales son el token, no se exponen aquí
                        userDetails.getAuthorities()
                );
                // Establecer detalles de la autenticación (como la IP del cliente)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Autenticar al usuario en el SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 5. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}

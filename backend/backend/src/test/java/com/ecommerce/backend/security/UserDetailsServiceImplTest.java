package com.ecommerce.backend.security;

import com.ecommerce.backend.model.Enum.Rol;
import com.ecommerce.backend.model.Usuario;
import com.ecommerce.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Usuario usuarioActivo;
    private Usuario usuarioInactivo;

    @BeforeEach
    void setUp() {
        usuarioActivo = Usuario.builder()
                .id(1L)
                .correoElectronico("active@example.com")
                .password("encodedPassword")
                .rol(Rol.CUSTOMER)
                .activo(true)
                .build();

        usuarioInactivo = Usuario.builder()
                .id(2L)
                .correoElectronico("inactive@example.com")
                .password("encodedPassword")
                .rol(Rol.CUSTOMER)
                .activo(false)
                .build();
    }

    @Test
    @DisplayName("Debe cargar UserDetails para un usuario activo existente")
    void debeCargarUsuarioActivo() {
        when(usuarioRepository.findByCorreoElectronico(anyString())).thenReturn(Optional.of(usuarioActivo));

        UserDetails userDetails = userDetailsService.loadUserByUsername("active@example.com");

        assertNotNull(userDetails);
        assertEquals("active@example.com", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER")));
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @DisplayName("Debe lanzar UsernameNotFoundException si el usuario no existe")
    void debeLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioRepository.findByCorreoElectronico(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    @DisplayName("Debe lanzar UsernameNotFoundException si el usuario está inactivo")
    void debeLanzarExcepcionSiUsuarioInactivo() {
        when(usuarioRepository.findByCorreoElectronico(anyString())).thenReturn(Optional.of(usuarioInactivo));

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("inactive@example.com"));
    }
}

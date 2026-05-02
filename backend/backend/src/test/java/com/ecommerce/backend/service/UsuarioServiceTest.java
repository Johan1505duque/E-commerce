package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.UsuarioDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.mapper.UsuarioMapper;
import com.ecommerce.backend.model.Enum.Rol;
import com.ecommerce.backend.model.Usuario;
import com.ecommerce.backend.repository.UsuarioRepository;
import com.ecommerce.backend.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private PasswordEncoder passwordEncoder; // Mock del PasswordEncoder

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nombre("Test")
                .apellido("User")
                .correoElectronico("test@example.com")
                .password("encodedPassword") // Ya codificada
                .rol(Rol.CUSTOMER)
                .activo(true)
                .build();

        usuarioDTO = UsuarioDTO.builder()
                .id(1L)
                .nombre("Test")
                .apellido("User")
                .correoElectronico("test@example.com")
                .password("rawPassword123") // Contraseña sin codificar
                .rol(Rol.CUSTOMER)
                .build();
    }

    @Test
    @DisplayName("Debe guardar un nuevo usuario y codificar la contraseña")
    void debeGuardarNuevoUsuario() {
        when(usuarioRepository.existsByCorreoElectronico(anyString())).thenReturn(false);
        when(usuarioMapper.toEntity(any(UsuarioDTO.class))).thenReturn(usuario);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        UsuarioDTO result = usuarioService.guardar(usuarioDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getCorreoElectronico());
        verify(passwordEncoder, times(1)).encode("rawPassword123"); // Verifica que se codificó
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si el correo ya está registrado al guardar")
    void debeLanzarExcepcionSiCorreoYaRegistradoAlGuardar() {
        when(usuarioRepository.existsByCorreoElectronico(anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> usuarioService.guardar(usuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe actualizar un usuario existente y codificar la nueva contraseña si se proporciona")
    void debeActualizarUsuario() {
        UsuarioDTO updateDTO = UsuarioDTO.builder()
                .nombre("Updated Name")
                .correoElectronico("updated@example.com")
                .password("newRawPassword")
                .rol(Rol.ADMIN)
                .build();

        Usuario existingUsuario = Usuario.builder()
                .id(1L)
                .nombre("Old Name")
                .apellido("Old Lastname")
                .correoElectronico("test@example.com")
                .password("oldEncodedPassword")
                .rol(Rol.CUSTOMER)
                .activo(true)
                .build();

        Usuario updatedUsuario = Usuario.builder()
                .id(1L)
                .nombre("Updated Name")
                .apellido("Old Lastname") // No se actualiza si no se proporciona en DTO
                .correoElectronico("updated@example.com")
                .password("newEncodedPassword")
                .rol(Rol.ADMIN)
                .activo(true)
                .build();

        UsuarioDTO updatedUsuarioDTO = UsuarioDTO.builder()
                .id(1L)
                .nombre("Updated Name")
                .apellido("Old Lastname")
                .correoElectronico("updated@example.com")
                .rol(Rol.ADMIN)
                .build();

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(existingUsuario));
        when(usuarioRepository.existsByCorreoElectronico("updated@example.com")).thenReturn(false); // Nuevo correo no existe
        when(passwordEncoder.encode("newRawPassword")).thenReturn("newEncodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(updatedUsuario);
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(updatedUsuarioDTO);

        UsuarioDTO result = usuarioService.actualizar(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Name", result.getNombre());
        assertEquals("updated@example.com", result.getCorreoElectronico());
        assertEquals(Rol.ADMIN, result.getRol());
        verify(passwordEncoder, times(1)).encode("newRawPassword");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el usuario no existe al actualizar")
    void debeLanzarExcepcionSiUsuarioNoExisteAlActualizar() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.actualizar(99L, usuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si el nuevo correo ya está registrado al actualizar")
    void debeLanzarExcepcionSiNuevoCorreoYaRegistradoAlActualizar() {
        UsuarioDTO updateDTO = UsuarioDTO.builder()
                .correoElectronico("existing@example.com")
                .build();

        Usuario existingUsuario = Usuario.builder()
                .id(1L)
                .correoElectronico("test@example.com")
                .build();

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(existingUsuario));
        when(usuarioRepository.existsByCorreoElectronico("existing@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> usuarioService.actualizar(1L, updateDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // Otros tests para buscarPorId, listarActivos, etc. (similar a los ya existentes)
}

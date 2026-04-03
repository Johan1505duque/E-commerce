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


import java.util.Optional;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nombre("Jhoan")
                .apellido("Duque")
                .correoElectronico("jhoan@test.com")
                .password("123456")
                .rol(Rol.CLIENTE)
                .activo(true)
                .build();

        usuarioDTO = UsuarioDTO.builder()
                .id(1L)
                .nombre("Jhoan")
                .apellido("Duque")
                .correoElectronico("jhoan@test.com")
                .rol(Rol.CLIENTE)
                .activo(true)
                .build();
    }

    @Test
    @DisplayName("Debe guardar usuario correctamente")
    void debeGuardarUsuario() {
        when(usuarioRepository
                .existsByCorreoElectronico(anyString()))
                .thenReturn(false);
        when(usuarioMapper.toEntity(any())).thenReturn(usuario);
        when(usuarioRepository.save(any())).thenReturn(usuario);
        when(usuarioMapper.toDTO(any())).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.guardar(usuarioDTO);

        assertNotNull(resultado);
        assertEquals("Jhoan", resultado.getNombre());
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si correo ya existe")
    void debeLanzarExcepcionCorreoDuplicado() {
        when(usuarioRepository
                .existsByCorreoElectronico(anyString()))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> usuarioService.guardar(usuarioDTO));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe buscar usuario por ID")
    void debeBuscarPorId() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(any())).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Debe lanzar excepción si usuario no existe")
    void debeLanzarExcepcionUsuarioNoEncontrado() {
        when(usuarioRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.buscarPorId(99L));
    }

    @Test
    @DisplayName("Debe desactivar usuario")
    void debeDesactivarUsuario() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenReturn(usuario);
        when(usuarioMapper.toDTO(any())).thenReturn(usuarioDTO);

        usuarioService.desactivar(1L);

        verify(usuarioRepository, times(1)).save(any());
    }
}

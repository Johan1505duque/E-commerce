package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.UsuarioDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Enum.Rol;
import com.ecommerce.backend.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioDTO usuarioDTO;
    private List<UsuarioDTO> usuarioDTOList;

    @BeforeEach
    void setUp() {
        usuarioDTO = UsuarioDTO.builder()
                .id(1L)
                .nombre("Test")
                .apellido("User")
                .correoElectronico("test@example.com")
                .password("password123")
                .rol(Rol.CUSTOMER)
                .build();

        usuarioDTOList = Arrays.asList(
                usuarioDTO,
                UsuarioDTO.builder().id(2L).nombre("Admin").correoElectronico("admin@example.com").rol(Rol.ADMIN).build()
        );
    }

    @Test
    @DisplayName("Debe listar usuarios activos y retornar 200 OK")
    void debeListarActivos() {
        when(usuarioService.listarActivos()).thenReturn(usuarioDTOList);

        ResponseEntity<ApiResponse<List<UsuarioDTO>>> response = usuarioController.listarActivos();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(usuarioDTOList.size(), response.getBody().getData().size());
        verify(usuarioService, times(1)).listarActivos();
    }

    @Test
    @DisplayName("Debe buscar usuario por ID y retornar 200 OK")
    void debeBuscarPorId() {
        when(usuarioService.buscarPorId(anyLong())).thenReturn(usuarioDTO);

        ResponseEntity<ApiResponse<UsuarioDTO>> response = usuarioController.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(usuarioDTO, response.getBody().getData());
        verify(usuarioService, times(1)).buscarPorId(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 NOT FOUND si usuario por ID no existe")
    void debeRetornarNotFoundSiUsuarioPorIdNoExiste() {
        when(usuarioService.buscarPorId(anyLong())).thenThrow(new ResourceNotFoundException("Usuario no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> usuarioController.buscarPorId(99L));
        verify(usuarioService, times(1)).buscarPorId(99L);
    }

    @Test
    @DisplayName("Debe crear un nuevo usuario y retornar 201 CREATED")
    void debeGuardarUsuario() {
        when(usuarioService.guardar(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        ResponseEntity<ApiResponse<UsuarioDTO>> response = usuarioController.guardar(usuarioDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(201, response.getBody().getStatus());
        assertEquals(usuarioDTO, response.getBody().getData());
        verify(usuarioService, times(1)).guardar(any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("Debe desactivar un usuario y retornar 200 OK")
    void debeDesactivarUsuario() {
        when(usuarioService.desactivar(anyLong())).thenReturn(usuarioDTO);

        ResponseEntity<ApiResponse<UsuarioDTO>> response = usuarioController.desactivar(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(usuarioDTO, response.getBody().getData());
        verify(usuarioService, times(1)).desactivar(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 NOT FOUND si usuario a desactivar no existe")
    void debeRetornarNotFoundSiUsuarioADesactivarNoExiste() {
        when(usuarioService.desactivar(anyLong())).thenThrow(new ResourceNotFoundException("Usuario no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> usuarioController.desactivar(99L));
        verify(usuarioService, times(1)).desactivar(99L);
    }
}

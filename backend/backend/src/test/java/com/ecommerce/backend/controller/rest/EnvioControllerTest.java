package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.CrearEnvioDTO;
import com.ecommerce.backend.dto.EnvioDTO;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Enum.EstadoEnvio;
import com.ecommerce.backend.service.EnvioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnvioControllerTest {

    @Mock
    private EnvioService envioService;

    @InjectMocks
    private EnvioController envioController;

    private EnvioDTO envioDTO;
    private CrearEnvioDTO crearEnvioDTO;

    @BeforeEach
    void setUp() {
        envioDTO = EnvioDTO.builder()
                .id(1L)
                .ordenId(10L)
                .direccionEnvio("Calle Falsa 123")
                .estado(EstadoEnvio.PENDIENTE)
                .codigoRastreo("TRACK123")
                .creacion(LocalDateTime.now())
                .actualizacion(LocalDateTime.now())
                .build();

        crearEnvioDTO = CrearEnvioDTO.builder()
                .ordenId(10L)
                .direccionEnvio("Calle Falsa 123")
                .build();
    }

    @Test
    @DisplayName("Debe crear un nuevo envío y retornar 201 CREATED")
    void debeCrearEnvio() {
        when(envioService.crearEnvio(any(CrearEnvioDTO.class))).thenReturn(envioDTO);

        ResponseEntity<ApiResponse<EnvioDTO>> response = envioController.crearEnvio(crearEnvioDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(201, response.getBody().getStatus());
        assertEquals(envioDTO, response.getBody().getData());
        verify(envioService, times(1)).crearEnvio(any(CrearEnvioDTO.class));
    }

    @Test
    @DisplayName("Debe obtener un envío por ID y retornar 200 OK")
    void debeObtenerEnvioPorId() {
        when(envioService.obtenerEnvioPorId(anyLong())).thenReturn(envioDTO);

        ResponseEntity<ApiResponse<EnvioDTO>> response = envioController.obtenerEnvioPorId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(envioDTO, response.getBody().getData());
        verify(envioService, times(1)).obtenerEnvioPorId(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 NOT FOUND si el envío por ID no existe")
    void debeRetornarNotFoundSiEnvioPorIdNoExiste() {
        when(envioService.obtenerEnvioPorId(anyLong())).thenThrow(new ResourceNotFoundException("Envío no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> envioController.obtenerEnvioPorId(99L));
        verify(envioService, times(1)).obtenerEnvioPorId(99L);
    }

    @Test
    @DisplayName("Debe obtener un envío por ID de orden y retornar 200 OK")
    void debeObtenerEnvioPorOrdenId() {
        when(envioService.obtenerEnvioPorOrdenId(anyLong())).thenReturn(envioDTO);

        ResponseEntity<ApiResponse<EnvioDTO>> response = envioController.obtenerEnvioPorOrdenId(10L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(envioDTO, response.getBody().getData());
        verify(envioService, times(1)).obtenerEnvioPorOrdenId(10L);
    }

    @Test
    @DisplayName("Debe retornar 404 NOT FOUND si el envío por ID de orden no existe")
    void debeRetornarNotFoundSiEnvioPorOrdenIdNoExiste() {
        when(envioService.obtenerEnvioPorOrdenId(anyLong())).thenThrow(new ResourceNotFoundException("Envío no encontrado para la orden"));

        assertThrows(ResourceNotFoundException.class, () -> envioController.obtenerEnvioPorOrdenId(99L));
        verify(envioService, times(1)).obtenerEnvioPorOrdenId(99L);
    }

    @Test
    @DisplayName("Debe actualizar el estado del envío y retornar 200 OK")
    void debeActualizarEstadoEnvio() {
        EnvioDTO updatedEnvioDTO = EnvioDTO.builder().id(1L).ordenId(10L).estado(EstadoEnvio.EN_CAMINO).build();
        when(envioService.actualizarEstadoEnvio(anyLong(), any(EstadoEnvio.class))).thenReturn(updatedEnvioDTO);

        ResponseEntity<ApiResponse<EnvioDTO>> response = envioController.actualizarEstadoEnvio(1L, EstadoEnvio.EN_CAMINO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(EstadoEnvio.EN_CAMINO, response.getBody().getData().getEstado());
        verify(envioService, times(1)).actualizarEstadoEnvio(1L, EstadoEnvio.EN_CAMINO);
    }

    @Test
    @DisplayName("Debe asignar código de rastreo a un envío y retornar 200 OK")
    void debeAsignarCodigoRastreo() {
        EnvioDTO updatedEnvioDTO = EnvioDTO.builder().id(1L).ordenId(10L).codigoRastreo("NEWTRACK456").build();
        when(envioService.asignarCodigoRastreo(anyLong(), anyString())).thenReturn(updatedEnvioDTO);

        ResponseEntity<ApiResponse<EnvioDTO>> response = envioController.asignarCodigoRastreo(1L, "NEWTRACK456");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals("NEWTRACK456", response.getBody().getData().getCodigoRastreo());
        verify(envioService, times(1)).asignarCodigoRastreo(1L, "NEWTRACK456");
    }

    @Test
    @DisplayName("Debe listar todos los envíos y retornar 200 OK")
    void debeListarTodosLosEnvios() {
        List<EnvioDTO> envios = Arrays.asList(envioDTO, EnvioDTO.builder().id(2L).ordenId(11L).build());
        when(envioService.listarTodosLosEnvios()).thenReturn(envios);

        ResponseEntity<ApiResponse<List<EnvioDTO>>> response = envioController.listarTodosLosEnvios();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(2, response.getBody().getData().size());
        verify(envioService, times(1)).listarTodosLosEnvios();
    }
}

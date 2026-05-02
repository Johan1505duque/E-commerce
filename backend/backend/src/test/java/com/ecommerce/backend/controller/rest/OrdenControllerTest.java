package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.ComOrdenDTO.CrearOrdenDTO;
import com.ecommerce.backend.dto.ComOrdenDTO.OrdenDTO;
import com.ecommerce.backend.model.Enum.EstadoOrden;
import com.ecommerce.backend.service.OrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenControllerTest {

    @Mock
    private OrdenService ordenService;

    @InjectMocks
    private OrdenController ordenController;

    private OrdenDTO ordenDTO;
    private CrearOrdenDTO crearOrdenDTO;
    private List<OrdenDTO> ordenDTOList;

    @BeforeEach
    void setUp() {
        ordenDTO = OrdenDTO.builder()
                .id(1L)
                .usuarioId(100L)
                .total(BigDecimal.valueOf(150.00))
                .estado(EstadoOrden.PENDIENTE)
                .creacion(LocalDateTime.now())
                .build();

        crearOrdenDTO = CrearOrdenDTO.builder()
                .usuarioId(100L)
                .direccionEnvio("Dir. de prueba")
                .items(Arrays.asList()) // Lista vacía para el test
                .build();

        ordenDTOList = Arrays.asList(
                ordenDTO,
                OrdenDTO.builder().id(2L).usuarioId(101L).total(BigDecimal.valueOf(200.00)).estado(EstadoOrden.EN_PROCESO).build()
        );
    }

    @Test
    @DisplayName("Debe crear una nueva orden y retornar 201 CREATED")
    void debeCrearOrden() {
        when(ordenService.crearOrden(any(CrearOrdenDTO.class))).thenReturn(ordenDTO);

        ResponseEntity<ApiResponse<OrdenDTO>> response = ordenController.crearOrden(crearOrdenDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(201, response.getBody().getStatus());
        assertEquals(ordenDTO, response.getBody().getData());
        verify(ordenService, times(1)).crearOrden(any(CrearOrdenDTO.class));
    }

    @Test
    @DisplayName("Debe buscar orden por ID y retornar 200 OK")
    void debeBuscarPorId() {
        when(ordenService.buscarPorId(anyLong())).thenReturn(ordenDTO);

        ResponseEntity<ApiResponse<OrdenDTO>> response = ordenController.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(ordenDTO, response.getBody().getData());
        verify(ordenService, times(1)).buscarPorId(1L);
    }

    @Test
    @DisplayName("Debe listar órdenes por usuario y retornar 200 OK")
    void debeListarPorUsuario() {
        when(ordenService.listarPorUsuario(anyLong())).thenReturn(ordenDTOList);

        ResponseEntity<ApiResponse<List<OrdenDTO>>> response = ordenController.listarPorUsuario(100L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(ordenDTOList.size(), response.getBody().getData().size());
        verify(ordenService, times(1)).listarPorUsuario(100L);
    }

    @Test
    @DisplayName("Debe listar órdenes por estado y retornar 200 OK")
    void debeListarPorEstado() {
        when(ordenService.listarPorEstado(any(EstadoOrden.class))).thenReturn(ordenDTOList);

        ResponseEntity<ApiResponse<List<OrdenDTO>>> response = ordenController.listarPorEstado(EstadoOrden.PENDIENTE);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(ordenDTOList.size(), response.getBody().getData().size());
        verify(ordenService, times(1)).listarPorEstado(EstadoOrden.PENDIENTE);
    }

    @Test
    @DisplayName("Debe cambiar el estado de una orden y retornar 200 OK")
    void debeCambiarEstado() {
        when(ordenService.cambiarEstado(anyLong(), any(EstadoOrden.class))).thenReturn(ordenDTO);

        ResponseEntity<ApiResponse<OrdenDTO>> response = ordenController.cambiarEstado(1L, EstadoOrden.EN_PROCESO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(ordenDTO, response.getBody().getData());
        verify(ordenService, times(1)).cambiarEstado(1L, EstadoOrden.EN_PROCESO);
    }

    @Test
    @DisplayName("Debe cancelar una orden y retornar 200 OK")
    void debeCancelarOrden() {
        when(ordenService.cancelarOrden(anyLong())).thenReturn(ordenDTO);

        ResponseEntity<ApiResponse<OrdenDTO>> response = ordenController.cancelar(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(ordenDTO, response.getBody().getData());
        verify(ordenService, times(1)).cancelarOrden(1L);
    }
}

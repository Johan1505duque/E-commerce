package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.ProductoDTO;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.service.ProductoService;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private ProductoDTO productoDTO;
    private List<ProductoDTO> productoDTOList;

    @BeforeEach
    void setUp() {
        productoDTO = ProductoDTO.builder()
                .id(1L)
                .nombre("Laptop")
                .precio(BigDecimal.valueOf(1200.00))
                .activo(true)
                .build();

        productoDTOList = Arrays.asList(
                productoDTO,
                ProductoDTO.builder().id(2L).nombre("Mouse").precio(BigDecimal.valueOf(25.00)).activo(true).build()
        );
    }

    @Test
    @DisplayName("Debe listar productos activos y retornar 200 OK")
    void debeListarActivos() {
        when(productoService.listarActivos()).thenReturn(productoDTOList);

        ResponseEntity<ApiResponse<List<ProductoDTO>>> response = productoController.listarActivos();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(productoDTOList.size(), response.getBody().getData().size());
        verify(productoService, times(1)).listarActivos();
    }

    @Test
    @DisplayName("Debe buscar producto por ID y retornar 200 OK")
    void debeBuscarPorId() {
        when(productoService.buscarPorId(anyLong())).thenReturn(productoDTO);

        ResponseEntity<ApiResponse<ProductoDTO>> response = productoController.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(productoDTO, response.getBody().getData());
        verify(productoService, times(1)).buscarPorId(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 NOT FOUND si producto por ID no existe")
    void debeRetornarNotFoundSiProductoPorIdNoExiste() {
        when(productoService.buscarPorId(anyLong())).thenThrow(new ResourceNotFoundException("Producto no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> productoController.buscarPorId(99L));
        verify(productoService, times(1)).buscarPorId(99L);
    }

    @Test
    @DisplayName("Debe buscar productos por nombre y retornar 200 OK")
    void debeBuscarPorNombre() {
        when(productoService.buscarPorNombre(anyString())).thenReturn(productoDTOList);

        ResponseEntity<ApiResponse<List<ProductoDTO>>> response = productoController.buscarPorNombre("Laptop");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(productoDTOList.size(), response.getBody().getData().size());
        verify(productoService, times(1)).buscarPorNombre("Laptop");
    }

    @Test
    @DisplayName("Debe buscar productos por rango de precio y retornar 200 OK")
    void debeBuscarPorRangoPrecio() {
        when(productoService.buscarPorRangoPrecio(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(productoDTOList);

        ResponseEntity<ApiResponse<List<ProductoDTO>>> response = productoController.buscarPorPrecio(BigDecimal.ZERO, BigDecimal.valueOf(10000));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(productoDTOList.size(), response.getBody().getData().size());
        verify(productoService, times(1)).buscarPorRangoPrecio(BigDecimal.ZERO, BigDecimal.valueOf(10000));
    }

    @Test
    @DisplayName("Debe crear un nuevo producto y retornar 201 CREATED")
    void debeGuardarProducto() {
        when(productoService.guardar(any(ProductoDTO.class))).thenReturn(productoDTO);

        ResponseEntity<ApiResponse<ProductoDTO>> response = productoController.guardar(productoDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(201, response.getBody().getStatus());
        assertEquals(productoDTO, response.getBody().getData());
        verify(productoService, times(1)).guardar(any(ProductoDTO.class));
    }

    @Test
    @DisplayName("Debe actualizar un producto existente y retornar 200 OK")
    void debeActualizarProducto() {
        when(productoService.actualizar(anyLong(), any(ProductoDTO.class))).thenReturn(productoDTO);

        ResponseEntity<ApiResponse<ProductoDTO>> response = productoController.actualizar(1L, productoDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(productoDTO, response.getBody().getData());
        verify(productoService, times(1)).actualizar(1L, productoDTO);
    }

    @Test
    @DisplayName("Debe desactivar un producto y retornar 200 OK")
    void debeDesactivarProducto() {
        when(productoService.desactivar(anyLong())).thenReturn(productoDTO);

        ResponseEntity<ApiResponse<ProductoDTO>> response = productoController.desactivar(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(productoDTO, response.getBody().getData());
        verify(productoService, times(1)).desactivar(1L);
    }
}

package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ProductoDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.mapper.ProductoMapper;
import com.ecommerce.backend.model.Producto;
import com.ecommerce.backend.repository.ProductoRepository;
import com.ecommerce.backend.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L)
                .nombre("Camiseta Nike")
                .precio(new BigDecimal("85000"))
                .activo(true)
                .build();

        productoDTO = ProductoDTO.builder()
                .id(1L)
                .nombre("Camiseta Nike")
                .precio(new BigDecimal("85000"))
                .activo(true)
                .build();
    }

    @Test
    @DisplayName("Debe guardar producto correctamente")
    void debeGuardarProducto() {
        when(productoRepository.existsByNombre(anyString()))
                .thenReturn(false);
        when(productoMapper.toEntity(any())).thenReturn(producto);
        when(productoRepository.save(any())).thenReturn(producto);
        when(productoMapper.toDTO(any())).thenReturn(productoDTO);

        ProductoDTO resultado = productoService.guardar(productoDTO);

        assertNotNull(resultado);
        assertEquals("Camiseta Nike", resultado.getNombre());
    }

    @Test
    @DisplayName("Debe lanzar excepción si nombre duplicado")
    void debeLanzarExcepcionNombreDuplicado() {
        when(productoRepository.existsByNombre(anyString()))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> productoService.guardar(productoDTO));
    }

    @Test
    @DisplayName("Debe listar productos activos")
    void debeListarActivos() {
        when(productoRepository.findByActivo(true))
                .thenReturn(List.of(producto));
        when(productoMapper.toDTOList(any()))
                .thenReturn(List.of(productoDTO));

        List<ProductoDTO> resultado = productoService.listarActivos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe buscar por rango de precio")
    void debeBuscarPorRangoPrecio() {
        when(productoRepository.findByActivoAndPrecioBetween(
                eq(true), any(), any()))
                .thenReturn(List.of(producto));
        when(productoMapper.toDTOList(any()))
                .thenReturn(List.of(productoDTO));

        List<ProductoDTO> resultado = productoService.buscarPorRangoPrecio(
                new BigDecimal("50000"),
                new BigDecimal("100000"));

        assertFalse(resultado.isEmpty());
    }
}

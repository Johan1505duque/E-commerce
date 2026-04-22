package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.InventarioDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.mapper.InventarioMapper;
import com.ecommerce.backend.model.Inventario;
import com.ecommerce.backend.repository.InventarioRepository;
import com.ecommerce.backend.repository.ProductoRepository;
import com.ecommerce.backend.service.impl.InventarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private InventarioMapper inventarioMapper;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Inventario inventario;
    private InventarioDTO inventarioDTO;

    @BeforeEach
    void setUp() {
        // En MongoDB el id es String
        inventario = Inventario.builder()
                .id("mongo-id-123")
                .productoId(1L)
                .cantidad(10)
                .ubicacionBodega("Bodega A")
                .build();

        inventarioDTO = InventarioDTO.builder()
                .id("mongo-id-123")
                .productoId(1L)
                .cantidad(10)
                .ubicacionBodega("Bodega A")
                .build();
    }

    @Test
    @DisplayName("Debe crear inventario correctamente")
    void debeCrearInventario() {
        // En el nuevo ServiceImpl usamos existsById del productoRepository
        when(productoRepository.existsById(1L))
                .thenReturn(true);
        when(inventarioRepository.existsByProductoId(1L))
                .thenReturn(false);
        when(inventarioRepository.save(any()))
                .thenReturn(inventario);
        when(inventarioMapper.toDTO(any()))
                .thenReturn(inventarioDTO);

        InventarioDTO resultado = inventarioService
                .guardar(1L, 10, "Bodega A");

        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());
        assertEquals("mongo-id-123", resultado.getId());
    }

    @Test
    @DisplayName("No debe crear inventario duplicado")
    void noDebeCrearInventarioDuplicado() {
        when(productoRepository.existsById(1L))
                .thenReturn(true);
        when(inventarioRepository.existsByProductoId(1L))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> inventarioService.guardar(1L, 10, "Bodega A"));
    }

    @Test
    @DisplayName("Debe actualizar cantidad correctamente")
    void debeActualizarCantidad() {
        when(inventarioRepository.findByProductoId(1L))
                .thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any()))
                .thenReturn(inventario);
        when(inventarioMapper.toDTO(any()))
                .thenReturn(inventarioDTO);

        InventarioDTO resultado = inventarioService
                .actualizarCantidad(1L, 20);

        assertNotNull(resultado);
    }

    @Test
    @DisplayName("No debe aceptar cantidad negativa")
    void noDebeAceptarCantidadNegativa() {
        assertThrows(BadRequestException.class,
                () -> inventarioService.actualizarCantidad(1L, -5));
    }
}

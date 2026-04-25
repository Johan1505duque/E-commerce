package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CarritoDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.mapper.CarritoMapper;
import com.ecommerce.backend.model.Carrito;
import com.ecommerce.backend.model.CarritoItem;
import com.ecommerce.backend.model.Producto;
import com.ecommerce.backend.repository.CarritoRepository;
import com.ecommerce.backend.repository.ProductoRepository;
import com.ecommerce.backend.service.impl.CarritoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CarritoMapper carritoMapper;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Long usuarioId;
    private Long productoId1;
    private Long productoId2;
    private Producto producto1;
    private Producto producto2;
    private Carrito carrito;
    private CarritoDTO carritoDTO;

    @BeforeEach
    void setUp() {
        usuarioId = 1L;
        productoId1 = 101L;
        productoId2 = 102L;

        producto1 = Producto.builder()
                .id(productoId1)
                .nombre("Laptop")
                .precio(BigDecimal.valueOf(1200.00))
                .build();

        producto2 = Producto.builder()
                .id(productoId2)
                .nombre("Mouse")
                .precio(BigDecimal.valueOf(25.00))
                .build();

        CarritoItem item1 = CarritoItem.builder()
                .productoId(productoId1)
                .nombreProducto("Laptop")
                .cantidad(1)
                .precioUnitario(BigDecimal.valueOf(1200.00))
                .build();

        carrito = Carrito.builder()
                .id("cart-id-1")
                .usuarioId(usuarioId)
                .items(new ArrayList<>(Arrays.asList(item1)))
                .build();
        carrito.calcularTotal();

        carritoDTO = CarritoDTO.builder()
                .id("cart-id-1")
                .usuarioId(usuarioId)
                .total(carrito.getTotal())
                .build();
    }

    @Test
    @DisplayName("Debe obtener un carrito existente por usuarioId")
    void debeObtenerCarritoExistente() {
        when(carritoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(carrito));
        when(carritoMapper.toDTO(any(Carrito.class))).thenReturn(carritoDTO);

        CarritoDTO result = carritoService.obtenerCarritoPorUsuario(usuarioId);

        assertNotNull(result);
        assertEquals(usuarioId, result.getUsuarioId());
        verify(carritoRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("Debe crear un carrito nuevo si no existe para el usuario")
    void debeCrearNuevoCarritoSiNoExiste() {
        when(carritoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(carritoMapper.toDTO(any(Carrito.class))).thenAnswer(invocation -> {
            Carrito newCart = invocation.getArgument(0);
            return CarritoDTO.builder().usuarioId(newCart.getUsuarioId()).total(newCart.getTotal()).build();
        });

        CarritoDTO result = carritoService.obtenerCarritoPorUsuario(usuarioId);

        assertNotNull(result);
        assertEquals(usuarioId, result.getUsuarioId());
        assertEquals(BigDecimal.ZERO, result.getTotal());
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    @DisplayName("Debe agregar un nuevo producto al carrito")
    void debeAgregarNuevoProducto() {
        when(carritoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(carrito));
        when(productoRepository.findById(productoId2)).thenReturn(Optional.of(producto2));
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(carritoMapper.toDTO(any(Carrito.class))).thenReturn(carritoDTO);

        CarritoDTO result = carritoService.agregarOActualizarProducto(usuarioId, productoId2, 2);

        assertNotNull(result);
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    @DisplayName("Debe actualizar la cantidad de un producto existente en el carrito")
    void debeActualizarCantidadProductoExistente() {
        when(carritoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(carrito));
        when(productoRepository.findById(productoId1)).thenReturn(Optional.of(producto1));
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(carritoMapper.toDTO(any(Carrito.class))).thenReturn(carritoDTO);

        CarritoDTO result = carritoService.agregarOActualizarProducto(usuarioId, productoId1, 3);

        assertNotNull(result);
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el producto no existe al agregar")
    void debeLanzarExcepcionSiProductoNoExiste() {
        when(carritoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(carrito));
        when(productoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> carritoService.agregarOActualizarProducto(usuarioId, 999L, 1));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si la cantidad es cero o negativa al agregar")
    void debeLanzarExcepcionSiCantidadEsCeroONegativa() {
        assertThrows(BadRequestException.class,
                () -> carritoService.agregarOActualizarProducto(usuarioId, productoId1, 0));
    }

    @Test
    @DisplayName("Debe eliminar un producto del carrito")
    void debeEliminarProducto() {
        when(carritoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(carritoMapper.toDTO(any(Carrito.class))).thenReturn(carritoDTO);

        CarritoDTO result = carritoService.eliminarProducto(usuarioId, productoId1);

        assertNotNull(result);
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    @DisplayName("Debe limpiar todos los productos del carrito")
    void debeLimpiarCarrito() {
        when(carritoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        carritoService.limpiarCarrito(usuarioId);

        assertTrue(carrito.getItems().isEmpty());
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    @DisplayName("Debe eliminar el carrito completo")
    void debeEliminarCarrito() {
        when(carritoRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(carrito));
        doNothing().when(carritoRepository).delete(any(Carrito.class));

        carritoService.eliminarCarrito(usuarioId);

        verify(carritoRepository, times(1)).delete(carrito);
    }
}

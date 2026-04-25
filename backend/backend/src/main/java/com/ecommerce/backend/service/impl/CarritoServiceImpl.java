package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.CarritoDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.mapper.CarritoMapper;
import com.ecommerce.backend.model.Carrito;
import com.ecommerce.backend.model.CarritoItem;
import com.ecommerce.backend.model.Producto;
import com.ecommerce.backend.repository.CarritoRepository;
import com.ecommerce.backend.repository.ProductoRepository;
import com.ecommerce.backend.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final CarritoMapper carritoMapper;

    @Override
    public CarritoDTO obtenerCarritoPorUsuario(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = Carrito.builder()
                            .usuarioId(usuarioId)
                            .build();
                    nuevoCarrito.calcularTotal();
                    return carritoRepository.save(nuevoCarrito);
                });
        return carritoMapper.toDTO(carrito);
    }

    @Override
    public CarritoDTO agregarOActualizarProducto(Long usuarioId, Long productoId, Integer cantidad) {
        if (cantidad <= 0) {
            throw new BadRequestException("La cantidad debe ser mayor a cero.");
        }

        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> Carrito.builder().usuarioId(usuarioId).build());

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + productoId));

        Optional<CarritoItem> existingItem = carrito.getItems().stream()
                .filter(item -> item.getProductoId().equals(productoId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setCantidad(cantidad);
        } else {
            CarritoItem newItem = CarritoItem.builder()
                    .productoId(producto.getId())
                    .nombreProducto(producto.getNombre())
                    .precioUnitario(producto.getPrecio())
                    .cantidad(cantidad)
                    .build();
            carrito.getItems().add(newItem);
        }

        carrito.setActualizacion(LocalDateTime.now());
        carrito.calcularTotal();
        return carritoMapper.toDTO(carritoRepository.save(carrito));
    }

    @Override
    public CarritoDTO eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario con ID: " + usuarioId));

        boolean removed = carrito.getItems().removeIf(item -> item.getProductoId().equals(productoId));

        if (!removed) {
            throw new ResourceNotFoundException("Producto con ID " + productoId + " no encontrado en el carrito del usuario " + usuarioId);
        }

        carrito.setActualizacion(LocalDateTime.now());
        carrito.calcularTotal();
        return carritoMapper.toDTO(carritoRepository.save(carrito));
    }

    @Override
    public void limpiarCarrito(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario con ID: " + usuarioId));
        carrito.getItems().clear();
        carrito.setActualizacion(LocalDateTime.now());
        carrito.calcularTotal();
        carritoRepository.save(carrito);
    }

    @Override
    public void eliminarCarrito(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario con ID: " + usuarioId));
        carritoRepository.delete(carrito);
    }
}

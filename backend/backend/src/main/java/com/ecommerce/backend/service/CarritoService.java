package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CarritoDTO;

public interface CarritoService {
    CarritoDTO obtenerCarritoPorUsuario(Long usuarioId);
    CarritoDTO agregarOActualizarProducto(Long usuarioId, Long productoId, Integer cantidad);
    CarritoDTO eliminarProducto(Long usuarioId, Long productoId);
    void limpiarCarrito(Long usuarioId);
    void eliminarCarrito(Long usuarioId);
}

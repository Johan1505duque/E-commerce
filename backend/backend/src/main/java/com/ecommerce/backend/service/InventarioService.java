package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.InventarioDTO;

import java.util.List;

public interface InventarioService {
    InventarioDTO guardar(Long productoId, Integer cantidad, String ubicacion);
    InventarioDTO buscarPorProducto(Long productoId);
    InventarioDTO actualizarCantidad(Long productoId, Integer cantidad);
    List<InventarioDTO> listarTodos();
    List<InventarioDTO> listarStockBajo(Integer minimo);
    boolean tieneStock(Long productoId);
}

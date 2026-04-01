package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ProductoDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {
    ProductoDTO guardar(ProductoDTO dto);
    ProductoDTO buscarPorId(Long id);
    List<ProductoDTO> listarActivos();
    List<ProductoDTO> buscarPorNombre(String nombre);
    List<ProductoDTO> buscarPorRangoPrecio(BigDecimal min, BigDecimal max);
    ProductoDTO actualizar(Long id, ProductoDTO dto);
    ProductoDTO desactivar(Long id);
}

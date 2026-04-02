package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.repository.*;
import com.ecommerce.backend.mapper.InventarioMapper;
import com.ecommerce.backend.exception.*;
import com.ecommerce.backend.model.Producto;
import com.ecommerce.backend.dto.InventarioDTO;
import com.ecommerce.backend.model.Inventario;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {
    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final InventarioMapper inventarioMapper;

    @Override
    public InventarioDTO guardar(Long productoId, Integer cantidad,
                                 String ubicacion) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + productoId));

        if (inventarioRepository.existsByProductoId(productoId)) {
            throw new BadRequestException(
                    "Ya existe inventario para el producto: "
                            + producto.getNombre());
        }

        Inventario inventario = Inventario.builder()
                .producto(producto)
                .cantidad(cantidad)
                .ubicacionBodega(ubicacion)
                .build();

        return inventarioMapper.toDTO(inventarioRepository.save(inventario));
    }

    @Override
    public InventarioDTO buscarPorProducto(Long productoId) {
        return inventarioMapper.toDTO(
                inventarioRepository.findByProductoId(productoId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Inventario no encontrado para producto: " + productoId)));
    }

    @Override
    public InventarioDTO actualizarCantidad(Long productoId, Integer cantidad) {
        if (cantidad < 0) {
            throw new BadRequestException(
                    "La cantidad no puede ser negativa");
        }
        Inventario inventario = inventarioRepository
                .findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventario no encontrado para producto: " + productoId));

        inventario.setCantidad(cantidad);
        return inventarioMapper.toDTO(inventarioRepository.save(inventario));
    }

    @Override
    public List<InventarioDTO> listarTodos() {
        return inventarioMapper.toDTOList(inventarioRepository.findAll());
    }

    @Override
    public List<InventarioDTO> listarStockBajo(Integer minimo) {
        return inventarioMapper.toDTOList(
                inventarioRepository.findByCantidadLessThan(minimo));
    }

    @Override
    public boolean tieneStock(Long productoId) {
        return inventarioRepository.findByProductoId(productoId)
                .map(inv -> inv.getCantidad() > 0)
                .orElse(false);
    }
}

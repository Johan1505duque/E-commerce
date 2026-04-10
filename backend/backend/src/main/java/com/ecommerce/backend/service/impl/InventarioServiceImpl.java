package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.service.InventarioService;
import com.ecommerce.backend.repository.InventarioRepository;
import com.ecommerce.backend.repository.ProductoRepository;
import com.ecommerce.backend.mapper.InventarioMapper;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.model.Inventario;
import com.ecommerce.backend.dto.InventarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final InventarioMapper inventarioMapper;

    @Override
    public InventarioDTO guardar(Long productoId, Integer cantidad, String ubicacion) {
        // Verificar que el producto exista en PostgreSQL
        if (!productoRepository.existsById(productoId)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + productoId);
        }

        // Verificar si ya existe inventario para este producto en MongoDB
        if (inventarioRepository.existsByProductoId(productoId)) {
            throw new BadRequestException("Ya existe inventario para el producto con ID: " + productoId);
        }

        // Crear nuevo inventario para MongoDB
        Inventario inventario = new Inventario();
        inventario.setProductoId(productoId);
        inventario.setCantidad(cantidad);
        inventario.setUbicacionBodega(ubicacion);
        inventario.setCreacion(LocalDateTime.now());
        inventario.setActualizacion(LocalDateTime.now());

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
            throw new BadRequestException("La cantidad no puede ser negativa");
        }
        
        Inventario inventario = inventarioRepository
                .findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventario no encontrado para producto: " + productoId));

        inventario.setCantidad(cantidad);
        inventario.setActualizacion(LocalDateTime.now());
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

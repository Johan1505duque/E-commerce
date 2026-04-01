package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.mapper.ProductoMapper;
import com.ecommerce.backend.repository.ProductoRepository;
import com.ecommerce.backend.service.ProductoService;
import com.ecommerce.backend.dto.ProductoDTO;
import com.ecommerce.backend.model.Producto;
import com.ecommerce.backend.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    public ProductoDTO guardar(ProductoDTO dto) {
        if (productoRepository.existsByNombre(dto.getNombre())) {
            throw new BadRequestException(
                    "Ya existe un producto con el nombre: " + dto.getNombre());
        }
        Producto producto = productoMapper.toEntity(dto);
        producto.setActivo(true);
        return productoMapper.toDTO(productoRepository.save(producto));
    }

    @Override
    public ProductoDTO buscarPorId(Long id) {
        return productoMapper.toDTO(
                productoRepository.findByIdAndActivo(id, true)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Producto no encontrado con id: " + id)));
    }

    @Override
    public List<ProductoDTO> listarActivos() {
        return productoMapper.toDTOList(
                productoRepository.findByActivo(true));
    }

    @Override
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        return productoMapper.toDTOList(
                productoRepository.findByNombreContainingIgnoreCase(nombre));
    }

    @Override
    public List<ProductoDTO> buscarPorRangoPrecio(
            BigDecimal min, BigDecimal max) {
        return productoMapper.toDTOList(
                productoRepository.findByActivoAndPrecioBetween(true, min, max));
    }

    @Override
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setImagenUrl(dto.getImagenUrl());
        return productoMapper.toDTO(productoRepository.save(producto));
    }

    @Override
    public ProductoDTO desactivar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));
        producto.setActivo(false);
        return productoMapper.toDTO(productoRepository.save(producto));
    }
}

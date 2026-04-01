package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.ProductoDTO;
import com.ecommerce.backend.model.Producto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductoMapper {
    public ProductoDTO toDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .imagenUrl(producto.getImagenUrl())
                .activo(producto.getActivo())
                .creacion(producto.getCreacion())
                .build();
    }

    public Producto toEntity(ProductoDTO dto) {
        return Producto.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .imagenUrl(dto.getImagenUrl())
                .activo(dto.getActivo())
                .build();
    }

    public List<ProductoDTO> toDTOList(List<Producto> productos) {
        return productos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

package com.ecommerce.backend.mapper;

import com.ecommerce.backend.model.Inventario;
import org.springframework.stereotype.Component;

import com.ecommerce.backend.dto.InventarioDTO;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class InventarioMapper {
    public InventarioDTO toDTO(Inventario inventario){
    return InventarioDTO.builder()
            .id(inventario.getId())
            .productoId(inventario.getProducto().getId())
            .productoNombre(inventario.getProducto().getNombre())
            .cantidad(inventario.getCantidad())
            .ubicacionBodega(inventario.getUbicacionBodega())
            .actualizacion(inventario.getActualizacion())
            .build();
}

public List<InventarioDTO> toDTOList(List<Inventario> inventarios) {
    return inventarios.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
}
}

package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.InventarioDTO;
import com.ecommerce.backend.model.Inventario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventarioMapper {

    public InventarioDTO toDTO(Inventario inventario) {
        if (inventario == null) {
            return null;
        }
        InventarioDTO dto = new InventarioDTO();
        dto.setId(inventario.getId());
        dto.setProductoId(inventario.getProductoId());
        dto.setCantidad(inventario.getCantidad());
        dto.setUbicacionBodega(inventario.getUbicacionBodega());
        dto.setCreacion(inventario.getCreacion());
        dto.setActualizacion(inventario.getActualizacion());
        return dto;
    }

    public List<InventarioDTO> toDTOList(List<Inventario> inventarios) {
        return inventarios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Inventario toEntity(InventarioDTO dto) {
        if (dto == null) {
            return null;
        }
        Inventario inventario = new Inventario();
        inventario.setProductoId(dto.getProductoId());
        inventario.setCantidad(dto.getCantidad());
        inventario.setUbicacionBodega(dto.getUbicacionBodega());
        inventario.setCreacion(dto.getCreacion() != null ? dto.getCreacion() : inventario.getCreacion());
        inventario.setActualizacion(dto.getActualizacion() != null ? dto.getActualizacion() : inventario.getActualizacion());
        return inventario;
    }

    public void updateEntityFromDto(InventarioDTO dto, Inventario inventario) {
        if (dto == null || inventario == null) {
            return;
        }
        if (dto.getProductoId() != null) {
            inventario.setProductoId(dto.getProductoId());
        }
        if (dto.getCantidad() != null) {
            inventario.setCantidad(dto.getCantidad());
        }
        if (dto.getUbicacionBodega() != null) {
            inventario.setUbicacionBodega(dto.getUbicacionBodega());
        }
    }
}
